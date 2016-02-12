package com.sr5initiative;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AddParticipantDialog.OnClickListener {

    private static final int INITIATIVE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        File file = new File(getFilesDir(), "participants.txt");
        if(file.exists()) {
            ParticipantArray.setInstance(file);
        }
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(new ParticipantAdapter(this));
        buttonSet();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case  R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ParticipantArray.saveInstance(new File(getFilesDir(), "participants.txt"));
    }


    public void addParticipant(View view) {
        AddParticipantDialog frag = new AddParticipantDialog();
        frag.show(getSupportFragmentManager(), "dialog");
    }


    private Bundle savedData;

    @Override
    public void onAddParticipantDialogClick(Bundle data) {
        if(data.getInt(AddParticipantDialog.WHICH) != AddParticipantDialog.NEGATIVE_BUTTON) {
            if(data.containsKey(AddParticipantDialog.ERROR)) {
                savedData = data;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(data.getString(AddParticipantDialog.ERROR))
                        .setTitle("Error")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AddParticipantDialog frag = new AddParticipantDialog();
                                frag.setArguments(savedData);
                                frag.show(getSupportFragmentManager(), "dialog");
                            }
                        });
                builder.create().show();
            } else {
                ListView lv = (ListView)findViewById(R.id.listView);
                ParticipantAdapter pa = (ParticipantAdapter)lv.getAdapter();
                pa.addParticipant(data.getString(AddParticipantDialog.NAME), data.getInt(AddParticipantDialog.INITIATIVE), data.getInt(AddParticipantDialog.TYPE));
                buttonSet();
                if(data.getInt(AddParticipantDialog.WHICH) == AddParticipantDialog.NEUTRAL_BUTTON) {
                    addParticipant(null);
                }
            }
        }
    }

    public void setCurrentActing(View view) {
        ListView lv = (ListView)findViewById(R.id.listView);
        ParticipantAdapter pa = (ParticipantAdapter)lv.getAdapter();
        int index = lv.getPositionForView(view);
        pa.setCurrent(index);
        lv.smoothScrollToPosition(index);
        buttonSet();
    }

    public void removeParticipant(View view) {
        ListView lv = (ListView) findViewById(R.id.listView);
        ParticipantAdapter pa = (ParticipantAdapter)lv.getAdapter();
        pa.removeParticipant(lv.getPositionForView(view));
        buttonSet();
    }

    private int indexClicked;

    public void initiativeAction(View view) {
        ListView lv = (ListView) findViewById(R.id.listView);
        indexClicked = lv.getPositionForView(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Initiative Action")
                .setCancelable(true)
                .setItems(R.array.initiative_actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initiativeActionChoosen(which);
                    }
                })
                .show();
    }

    private void initiativeActionChoosen(int which) {
        int[] results = getResources().getIntArray(R.array.initiave_action_results);
        ParticipantArray.getInstance().doAction(indexClicked, results[which]);
        ListView lv = (ListView) findViewById(R.id.listView);
        ParticipantAdapter pa = (ParticipantAdapter)lv.getAdapter();
        pa.notifyDataSetInvalidated();
    }

    private void buttonSet() {
        ListView lv = (ListView)findViewById(R.id.listView);
        ParticipantAdapter pa = (ParticipantAdapter)lv.getAdapter();
        findViewById(R.id.button_next_pass).setEnabled(pa.onLastParticipant() && !pa.onLastPass());
        findViewById(R.id.button_new_round).setEnabled(pa.onLastParticipant() && pa.onLastPass());
    }

    public void nextThing(View view) {
        ListView lv = (ListView)findViewById(R.id.listView);
        ParticipantAdapter pa = (ParticipantAdapter)lv.getAdapter();
        switch(view.getId()) {
            case R.id.button_next_act:
                pa.nextParticipant();
                buttonSet();
                break;
            case R.id.button_next_pass:
                pa.nextPass();
                buttonSet();
                break;
            case R.id.button_new_round:
                String setInitiativeMode = PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsFragment.KEY_PREF_SET_INI, "");
                switch(setInitiativeMode) {
                    case SettingsFragment.DIALOG_CHAIN:
                        ParticipantInitiativeSetDialogChain.doChain(this, getSupportFragmentManager());
                        break;
                    case SettingsFragment.SINGLE_ACTIVITY:
                        Intent intent = new Intent(this, SetInitiativeActivity.class);
                        startActivityForResult(intent, INITIATIVE_REQUEST);
                        break;
                    default:
                        System.out.println("setInitiativeMode = " +setInitiativeMode);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INITIATIVE_REQUEST) {
            if(resultCode == RESULT_OK) {
                newRoundContinue();
            }
        }
    }

    public void newRoundContinue() {
        ListView lv = (ListView)findViewById(R.id.listView);
        ParticipantAdapter pa = (ParticipantAdapter)lv.getAdapter();
        pa.nextRound();
        buttonSet();
    }

    public void removeGroup(View view) {
        ListView lv = (ListView)findViewById(R.id.listView);
        ParticipantAdapter pa = (ParticipantAdapter)lv.getAdapter();
        switch(view.getId()) {
            case R.id.button_remove_enemies:
                pa.removeEnemies();
                break;
            case R.id.button_remove_others:
                pa.removeOthers();
                break;
        }
        buttonSet();
    }
}