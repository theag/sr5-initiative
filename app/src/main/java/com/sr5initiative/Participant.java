package com.sr5initiative;

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
        return new Participant(tokens.nextToken(), Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()));
    }

    public final String name;
    private int initiative;
    private int passInitiative;
    public int type;

    public Participant(String name, int initiative, int type) {
        this.name = name;
        this.initiative = initiative;
        passInitiative = initiative;
        this.type = type;
    }

    public int nextPass() {
        passInitiative -= 10;
        return passInitiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
        passInitiative = initiative;
    }

    public void resetInitiative() {
        passInitiative = initiative;
    }

    @Override
    public int compareTo(Participant another) {
        if(passInitiative != another.passInitiative) {
            return another.passInitiative - passInitiative;
        } else {
            return type - another.type;
        }
    }

    public int getPassInitiative() {
        return passInitiative;
    }

    public String save() {
        return name +((char)31) +initiative +((char)31) +type;
    }
}
