package net.egartley.beyondorigins.ingame;

/**
 * Something the player must do to progress in a quest
 */
public class QuestObjective {

    public boolean isComplete;
    public String title;
    public String description;

    public QuestObjective(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void complete() {
        boolean first = !isComplete;
        isComplete = true;
        if (first) {
            onCompletion();
        }
    }

    public void onCompletion() {

    }

    @Override
    public String toString() {
        return title + ": " + description;
    }

}
