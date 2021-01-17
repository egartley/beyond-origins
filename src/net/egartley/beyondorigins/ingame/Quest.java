package net.egartley.beyondorigins.ingame;

import java.util.ArrayList;

/**
 * A list of tasks, or objectives, the player should complete. Shown in the player menu in the quests tab
 */
public class Quest {

    public byte id;
    public boolean didStart;
    public boolean isComplete;
    public String title;
    public String description;
    public ArrayList<QuestObjective> objectives;

    public Quest(byte id, String title, String description) {
        this(id, title, description, new ArrayList<>());
    }

    public Quest(byte id, String title, String description, ArrayList<QuestObjective> objectives) {
        this.id = id;
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

    /**
     * Marks the quest as complete, and makes sure all objectives are complete as well
     */
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

    /**
     * Returns true if the quest has started, but not completed, false otherwise
     * @return True if the quest has started but not completed, false otherwise
     */
    public boolean inProgress() {
        return didStart && !isComplete;
    }

    @Override
    public String toString() {
        return title;
    }

}
