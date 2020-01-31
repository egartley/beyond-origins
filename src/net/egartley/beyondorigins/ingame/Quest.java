package net.egartley.beyondorigins.ingame;

import java.util.ArrayList;

public class Quest {

    public String title;
    public String description;
    public ArrayList<QuestObjective> objectives;

    public boolean isComplete;
    public boolean didStart;

    public Quest(String title, String description) {
        this(title, description, new ArrayList<>());
    }

    public Quest(String title, String description, ArrayList<QuestObjective> objectives) {
        this.title = title;
        this.description = description;
        this.objectives = objectives;
    }

    public void onStart() {

    }

    public void onComplete() {

    }

    public void start() {
        if (!didStart) {
            onStart();
        }
        didStart = true;
    }

    public void complete() {
        if (!isComplete) {
            onComplete();
        }
        isComplete = true;
        for (QuestObjective objective : objectives) {
            // just to make sure all the objectives are marked as complete
            objective.complete();
        }
    }

    public boolean inProgress() {
        return didStart && !isComplete;
    }

    @Override
    public String toString() {
        return title;
    }

}
