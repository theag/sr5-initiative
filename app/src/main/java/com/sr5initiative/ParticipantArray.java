package com.sr5initiative;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by nbp184 on 2016/02/08.
 */
public class ParticipantArray implements Iterable<Participant> {

    private static ParticipantArray instance = null;

    public static ParticipantArray getInstance() {
        if(instance == null) {
            instance = new ParticipantArray();
        }
        return instance;
    }

    public static void setInstance(File file) {
        instance = new ParticipantArray();
        try {
            BufferedReader inFile = new BufferedReader(new FileReader(file));
            String line = inFile.readLine();
            while(line != null) {
                instance.participants.add(Participant.load(line));
                line = inFile.readLine();
            }
            inFile.close();
            Collections.sort(instance.participants);
        } catch (IOException | NumberFormatException e) {
            instance.participants.clear();
            file.delete();
        }
    }

    public static void saveInstance(File file) {
        if(instance != null) {
            try {
                PrintWriter outFile = new PrintWriter(file);
                for (Participant p : instance.participants) {
                    outFile.println(p.save());
                }
                for (Participant p : instance.nextRound) {
                    outFile.println(p.save());
                }
                outFile.close();
            } catch (IOException e) {
                file.delete();
            }
        }
    }

    private ArrayList<Participant> participants;
    private ArrayList<Participant> nextRound;
    private int current;

    private ParticipantArray() {
        participants = new ArrayList<>();
        nextRound = new ArrayList<>();
        current = -1;
    }

    public int size() {
        return participants.size();
    }

    public Participant get(int index) {
        return participants.get(index);
    }

    @Override
    public Iterator<Participant> iterator() {
        return participants.iterator();
    }

    public int getCurrent() {
        return current;
    }

    public void add(Participant p) {
        participants.add(p);
        Collections.sort(participants);
    }

    public void nextParticipant() {
        current++;
        if(current == participants.size()) {
            current = -1;
        }
    }

    public boolean onLastParticipant() {
        return !participants.isEmpty() && current == participants.size()-1;
    }

    public boolean onLastPass() {
        if(participants.isEmpty()) {
            return false;
        }
        for(Participant p : participants) {
            if(p.getPassInitiative() > 10) {
                return false;
            }
        }
        return true;
    }

    public void nextPass() {
        for(Participant p : participants) {
            p.nextPass();
            if(p.getPassInitiative() <= 0) {
                nextRound.add(p);
            }
        }
        for(Participant p : nextRound) {
            participants.remove(p);
        }
        current = -1;
    }

    public Participant[] getAllParticipants() {
        Participant[] rv = new Participant[participants.size()+nextRound.size()];
        for(int i = 0; i < participants.size(); i++) {
            rv[i] = participants.get(i);
        }
        for(int i = 0; i < nextRound.size(); i++) {
            rv[i+participants.size()] = nextRound.get(i);
        }
        return rv;
    }

    public void nextRound() {
        participants.addAll(nextRound);
        nextRound.clear();
        Collections.sort(participants);
        current = -1;
    }

    public void remove(int index) {
        participants.remove(index);
        if(current > index) {
            current--;
        }
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void removeEnemies() {
        for(int i = 0; i < participants.size(); i++) {
            if(participants.get(i).type == Participant.ENEMY) {
                participants.remove(i);
            }
        }
        for(int i = 0; i < nextRound.size(); i++) {
            if(nextRound.get(i).type == Participant.ENEMY) {
                nextRound.remove(i);
            }
        }
        current = -1;
    }

    public void removeOthers() {
        for(int i = 0; i < participants.size(); i++) {
            if(participants.get(i).type != Participant.PLAYER) {
                participants.remove(i);
            }
        }
        for(int i = 0; i < nextRound.size(); i++) {
            if(nextRound.get(i).type != Participant.PLAYER) {
                nextRound.remove(i);
            }
        }
        current = -1;
    }

    public void doAction(int index, int adjustment) {
        participants.get(index).setInitiative(participants.get(index).getPassInitiative() - adjustment);
    }
}
