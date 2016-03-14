package com.sr5initiative;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by nbp184 on 2016/02/05.
 */
public class ParticipantAdapter extends BaseAdapter implements ListAdapter {

    private ParticipantArray participants;
    private Context context;

    public ParticipantAdapter(Context context) {
        participants = ParticipantArray.getInstance();
        this.context = context;
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Override
    public Object getItem(int position) {
        return participants.get(position);
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
            view = inflater.inflate(R.layout.list_item_participant, null);
        }
        Participant current = participants.get(position);
        switch(current.type) {
            case Participant.PLAYER:
                view.setBackgroundColor(getColor(R.color.player));
                break;
            case Participant.NPC:
                view.setBackgroundColor(getColor(R.color.npc));
                break;
            case Participant.ENEMY:
                view.setBackgroundColor(getColor(R.color.enemy));
                break;
        }
        boolean hasUniqueInitiative = true;
        for(Participant p : participants) {
            if(p != current && p.getPassInitiative() == current.getPassInitiative()) {
                hasUniqueInitiative = false;
                break;
            }
        }
        if(hasUniqueInitiative) {
            view.findViewById(R.id.image_shares_initiative).setVisibility(View.INVISIBLE);
        } else {
            view.findViewById(R.id.image_shares_initiative).setVisibility(View.VISIBLE);
        }
        TextView tv = (TextView)view.findViewById(R.id.text_name);
        if(hasUniqueInitiative) {
            tv.setText(current.name);
        } else {
            tv.setText(current.name +" (" +current.reaction +")");
        }
        if(position == participants.getCurrent()) {
            tv.setBackgroundColor(getColor(R.color.current));
            tv.setTextColor(getColor(android.R.color.black));
        } else {
            tv.setBackgroundColor(getColor(android.R.color.transparent));
            tv.setTextColor(getColor(android.R.color.white));
        }
        tv = (TextView)view.findViewById(R.id.text_initiative);
        tv.setText(""+current.getPassInitiative());
        return view;
    }

    private int getColor(int resID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(resID);
        } else {
            return context.getResources().getColor(resID);
        }
    }

    public void addParticipant(Participant p) {
        participants.add(p);
        notifyDataSetInvalidated();
    }

    public void addParticipant(String name, int reaction, int initiative, int type) {
        participants.add(new Participant(name, reaction, initiative, type));
        notifyDataSetInvalidated();
    }

    public void nextParticipant() {
        participants.nextParticipant();
        notifyDataSetChanged();
    }

    public boolean onLastParticipant() {
        return participants.onLastParticipant();
    }

    public boolean onLastPass() {
        return participants.onLastPass();
    }

    public void nextPass() {
        participants.nextPass();
        notifyDataSetChanged();
    }

    public void nextRound() {
        participants.nextRound();
        notifyDataSetInvalidated();
    }

    public void removeParticipant(int index) {
        participants.remove(index);
        notifyDataSetInvalidated();
    }

    public Participant getParticipant(int index) {
        return participants.get(index);
    }

    public void setCurrent(int current) {
        participants.setCurrent(current);
        notifyDataSetChanged();
    }

    public void removeEnemies() {
        participants.removeEnemies();
        notifyDataSetInvalidated();
    }

    public void removeOthers() {
        participants.removeOthers();
        notifyDataSetInvalidated();
    }
}
