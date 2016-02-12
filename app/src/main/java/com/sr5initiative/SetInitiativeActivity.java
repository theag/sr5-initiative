package com.sr5initiative;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class SetInitiativeActivity extends AppCompatActivity {

    public static final String PARTICIPANTS = "participants";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_initiative);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.set_initiative);
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(new SetInitiativeAdapter(this));
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void done(View view) {
        ListView lv = (ListView)findViewById(R.id.listView);
        SetInitiativeAdapter sia = (SetInitiativeAdapter)lv.getAdapter();
        sia.setInitiatives();
        setResult(RESULT_OK);
        finish();
    }
}
