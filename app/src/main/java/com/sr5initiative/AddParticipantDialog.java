package com.sr5initiative;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by nbp184 on 2016/02/05.
 */
public class AddParticipantDialog extends DialogFragment {

    public interface OnClickListener {
        void onAddParticipantDialogClick(Bundle data);
    }

    public static final String WHICH = "which";
    public static final String NAME = "name";
    public static final String REACTION = "reaction";
    public static final String INITIATIVE = "initiative";
    public static final String TYPE = "type";
    public static final String ERROR = "error";

    public static final int POSITIVE_BUTTON = 1;
    public static final int NEUTRAL_BUTTON = 0;
    public static final int NEGATIVE_BUTTON = -1;

    private OnClickListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnClickListener) {
            listener = (OnClickListener)activity;
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflated = inflater.inflate(R.layout.dialog_add_participant, null);
        Bundle args = getArguments();
        if(args != null) {
            EditText et = (EditText)inflated.findViewById(R.id.editText_initiative);
            if(args.containsKey(INITIATIVE)) {
                et.setText("" + args.getInt(INITIATIVE));
            } else {
                et.requestFocus();
            }
            et = (EditText)inflated.findViewById(R.id.editText_reaction);
            if(args.containsKey(REACTION)) {
                et.setText(args.getString(REACTION));
            } else {
                et.requestFocus();
            }
            et = (EditText)inflated.findViewById(R.id.editText_name);
            if(args.containsKey(NAME)) {
                et.setText(args.getString(NAME));
            } else {
                et.requestFocus();
            }
            RadioButton rb;
            switch(args.getInt(TYPE)) {
                case Participant.PLAYER:
                    rb = (RadioButton)inflated.findViewById(R.id.radio_player);
                    break;
                case Participant.NPC:
                    rb = (RadioButton)inflated.findViewById(R.id.radio_npc);
                    break;
                case Participant.ENEMY:
                default:
                    rb = (RadioButton)inflated.findViewById(R.id.radio_enemy);
                    break;
            }
            rb.setChecked(true);
        }
        builder.setView(inflated)
            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(WHICH, POSITIVE_BUTTON);
                    Dialog apd = getDialog();
                    EditText et = (EditText)apd.findViewById(R.id.editText_initiative);
                    if(et.getText().length() == 0) {
                        bundle.putString(ERROR, "Must set initiative.");
                    } else {
                        bundle.putInt(INITIATIVE, Integer.parseInt(et.getText().toString()));
                    }
                    et = (EditText)apd.findViewById(R.id.editText_reaction);
                    if(et.getText().length() == 0) {
                        bundle.putString(ERROR, "Must set reaction.");
                    } else {
                        bundle.putInt(REACTION, Integer.parseInt(et.getText().toString()));
                    }
                    et = (EditText)apd.findViewById(R.id.editText_name);
                    if(et.getText().length() == 0) {
                        bundle.putString(ERROR, "Must set name.");
                    } else {
                        bundle.putString(NAME, et.getText().toString());
                    }
                    RadioGroup rg = (RadioGroup)apd.findViewById(R.id.radioGroup_type);
                    switch(rg.getCheckedRadioButtonId()) {
                        case R.id.radio_player:
                            bundle.putInt(TYPE, Participant.PLAYER);
                            break;
                        case R.id.radio_npc:
                            bundle.putInt(TYPE, Participant.NPC);
                            break;
                        case R.id.radio_enemy:
                            bundle.putInt(TYPE, Participant.ENEMY);
                            break;
                    }
                    listener.onAddParticipantDialogClick(bundle);
                }
            })
            .setNeutralButton("Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(WHICH, NEUTRAL_BUTTON);
                    Dialog apd = getDialog();
                    EditText et = (EditText)apd.findViewById(R.id.editText_initiative);
                    if(et.getText().length() == 0) {
                        bundle.putString(ERROR, "Must set initiative.");
                    } else {
                        bundle.putInt(INITIATIVE, Integer.parseInt(et.getText().toString()));
                    }
                    et = (EditText)apd.findViewById(R.id.editText_reaction);
                    if(et.getText().length() == 0) {
                        bundle.putString(ERROR, "Must set reaction.");
                    } else {
                        bundle.putInt(REACTION, Integer.parseInt(et.getText().toString()));
                    }
                    et = (EditText)apd.findViewById(R.id.editText_name);
                    if(et.getText().length() == 0) {
                        bundle.putString(ERROR, "Must set name.");
                    } else {
                        bundle.putString(NAME, et.getText().toString());
                    }
                    RadioGroup rg = (RadioGroup)apd.findViewById(R.id.radioGroup_type);
                    switch(rg.getCheckedRadioButtonId()) {
                        case R.id.radio_player:
                            bundle.putInt(TYPE, Participant.PLAYER);
                            break;
                        case R.id.radio_npc:
                            bundle.putInt(TYPE, Participant.NPC);
                            break;
                        case R.id.radio_enemy:
                            bundle.putInt(TYPE, Participant.ENEMY);
                            break;
                    }
                    listener.onAddParticipantDialogClick(bundle);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(WHICH, NEGATIVE_BUTTON);
                    listener.onAddParticipantDialogClick(bundle);
                }
            });
        return builder.create();
    }

}
