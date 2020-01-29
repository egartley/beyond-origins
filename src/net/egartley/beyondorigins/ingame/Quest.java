package net.egartley.beyondorigins.ingame;

import java.util.ArrayList;

public class Quest {

    public String name;
    public ArrayList<QuestObjective> objectives;

    public boolean isComplete;
    public boolean didStart;

    public Quest(String name) {
        this(name, new ArrayList<>());
    }

    public Quest(String name, ArrayList<QuestObjective> objectives) {
        this.name = name;
        this.objectives = objectives;
    }

    public void start() {
        didStart = true;
    }

    public void complete() {
        isComplete = true;
    }

    public boolean inProgress() {
        return didStart && !isComplete;
    }

    @Override
    public String toString() {
        return name;
    }

}
