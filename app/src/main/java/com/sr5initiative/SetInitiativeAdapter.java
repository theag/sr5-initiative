package com.sr5initiative;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by nbp184 on 2016/02/05.
 */
public class SetInitiativeAdapter extends BaseAdapter implements ListAdapter {

    private Participant[] participants;
    private String[] storedInitiatives;
    private Context context;

    public SetInitiativeAdapter(Context context) {
        participants = ParticipantArray.getInstance().getAllParticipants();
        storedInitiatives = new String[participants.length];
        for(int i = 0; i < storedInitiatives.length; i++) {
            storedInitiatives[i] = "";
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return participants.length;
    }

    @Override
    public Object getItem(int position) {
        return participants[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_set_initiative, null);
        }
        Participant current = participants[position];
        TextView tv = (TextView)view.findViewById(R.id.text_name);
        tv.setText(current.name);
        tv = (TextView)view.findViewById(R.id.text_type);
        switch(current.type) {
            case Participant.PLAYER:
                tv.setText("(" +context.getString(R.string.player) +")");
                break;
            case Participant.NPC:
                tv.setText("(" +context.getString(R.string.npc) +")");
                break;
            case Participant.ENEMY:
                tv.setText("(" +context.getString(R.string.enemy) +")");
                break;
        }
        View subView = view.findViewById(R.id.editText_initiative);
        subView.setTag(position);
        subView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    storedInitiatives[(int)v.getTag()] = ((EditText)v).getText().toString();
                }
            }
        });
        return view;
    }

    public Participant getParticipant(int index) {
        return participants[index];
    }

    public void setInitiatives() {
        for(int i = 0; i < participants.length; i++) {
            if(storedInitiatives[i].isEmpty()) {
                participants[i].resetInitiative();
            } else {
                participants[i].setInitiative(Integer.parseInt(storedInitiatives[i]));
            }
        }
    }
}
