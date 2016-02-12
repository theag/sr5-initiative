package com.sr5initiative;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by nbp184 on 2016/02/05.
 */
public class ParticipantInitiativeSetDialogChain extends DialogFragment {

    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String IS_LAST = "is last";

    private static Participant[] participants;
    private static FragmentManager fragmentManager;
    private static MainActivity activity;
    private static int index;

    public static void doChain(MainActivity activity, FragmentManager fragmentManager) {
        index = 0;
        ParticipantInitiativeSetDialogChain.participants = ParticipantArray.getInstance().getAllParticipants();
        ParticipantInitiativeSetDialogChain.fragmentManager = fragmentManager;
        ParticipantInitiativeSetDialogChain.activity = activity;
        ParticipantInitiativeSetDialogChain pisdc = new ParticipantInitiativeSetDialogChain();
        Bundle args = new Bundle();
        args.putString(NAME, participants[index].name);
        args.putInt(TYPE, participants[index].type);
        args.putBoolean(IS_LAST, participants.length == 1);
        pisdc.setArguments(args);
        pisdc.show(fragmentManager, "dialog");
    }

    private static void showNext(String initiative) {
        if(initiative.isEmpty()) {
            participants[index].resetInitiative();
        } else {
            participants[index].setInitiative(Integer.parseInt(initiative));
        }
        if(index < participants.length-1) {
            index++;
            ParticipantInitiativeSetDialogChain pisdc = new ParticipantInitiativeSetDialogChain();
            Bundle args = new Bundle();
            args.putString(NAME, participants[index].name);
            args.putInt(TYPE, participants[index].type);
            args.putBoolean(IS_LAST, index == participants.length - 1);
            pisdc.setArguments(args);
            pisdc.show(fragmentManager, "dialog");
        } else {
            activity.newRoundContinue();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflated = inflater.inflate(R.layout.dialog_set_initiative, null);
        Bundle data = getArguments();
        TextView tv = (TextView)inflated.findViewById(R.id.text_name);
        tv.setText(data.getString(NAME));
        tv = (TextView)inflated.findViewById(R.id.text_type);
        switch(data.getInt(TYPE)) {
            case Participant.PLAYER:
                tv.setText("(" +getString(R.string.player) +")");
                break;
            case Participant.NPC:
                tv.setText("(" +getString(R.string.npc) +")");
                break;
            case Participant.ENEMY:
                tv.setText("(" +getString(R.string.enemy) +")");
                break;
        }
        builder.setView(inflated);
        if(data.getBoolean(IS_LAST)) {
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText et = (EditText)ParticipantInitiativeSetDialogChain.this.getDialog().findViewById(R.id.editText_initiative);
                    ParticipantInitiativeSetDialogChain.showNext(et.getText().toString());
                }
            });
        } else {
            builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText et = (EditText)ParticipantInitiativeSetDialogChain.this.getDialog().findViewById(R.id.editText_initiative);
                    ParticipantInitiativeSetDialogChain.showNext(et.getText().toString());
                }
            });
        }
        return builder.create();
    }

}
