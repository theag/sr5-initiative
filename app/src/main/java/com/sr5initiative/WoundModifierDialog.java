package com.sr5initiative;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Thea on 2016-03-17.
 */
public class WoundModifierDialog extends DialogFragment {

    public static final String INDEX = "index";

    public interface OnClickListener {
        void onWoundPenaltyDialogClick(int value);
    }

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
        View inflated = inflater.inflate(R.layout.dialog_wound_penalty, null);
        Bundle args = getArguments();
        NumberPicker np = (NumberPicker)inflated.findViewById(R.id.numberPicker);
        np.setMaxValue(10);
        np.setMinValue(0);
        np.setDisplayedValues(new String[]{"0", "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10"});
        if(args != null) {
            int index = args.getInt(INDEX);
            np.setValue(-ParticipantArray.getInstance().get(index).getWoundPenalty());
        }
        builder.setView(inflated)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NumberPicker np = (NumberPicker)getDialog().findViewById(R.id.numberPicker);
                        listener.onWoundPenaltyDialogClick(-np.getValue());
                    }
                })
                .setCancelable(true);
        return builder.create();
    }

}
