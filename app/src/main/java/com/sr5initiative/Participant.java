package com.sr5initiative;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by nbp184 on 2016/02/05.
 */
public class Participant implements Comparable<Participant> {

    public static final int PLAYER = 0;
    public static final int NPC = 1;
    public static final int ENEMY = 2;

    public static Participant load(String data) {
        StringTokenizer tokens = new StringTokenizer(data, ""+((char)31));
        return new Participant(tokens.nextToken(), Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()));
    }

    public final String name;
    public final int reaction;
    private int initiative;
    private int passInitiative;
    public int type;
    private int lastAction;
    private int woundPenalty;

    public Participant(String name, int reaction, int initiative, int type) {
        this.name = name;
        this.reaction = reaction;
        this.initiative = initiative;
        passInitiative = initiative;
        this.type = type;
        lastAction = 0;
        woundPenalty = 0;
    }

    public int nextPass() {
        passInitiative -= 10;
        return passInitiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative - woundPenalty;
        passInitiative = this.initiative;
        lastAction = 0;
    }

    public void resetInitiative() {
        passInitiative = initiative;
        lastAction = 0;
    }

    @Override
    public int compareTo(Participant another) {
        if(passInitiative != another.passInitiative) {
            return another.passInitiative - passInitiative;
        } else if(reaction != another.reaction) {
            return another.reaction - reaction;
        } else {
            return type - another.type;
        }
    }

    public int getPassInitiative() {
        return passInitiative;
    }

    public String save() {
        return name +((char)31) +reaction +((char)31) +initiative +((char)31) +type;
    }

    public void doAction(int adjustment) {
        lastAction = adjustment;
        passInitiative -= adjustment;
    }

    public void undoAction() {
        passInitiative += lastAction;
        lastAction = 0;
    }

    public void setWoundPenalty(int woundPenalty) {
        initiative += woundPenalty - this.woundPenalty;
        passInitiative += woundPenalty - this.woundPenalty;
        this.woundPenalty = woundPenalty;
    }

    public int getWoundPenalty() {
        return woundPenalty;
    }
}
