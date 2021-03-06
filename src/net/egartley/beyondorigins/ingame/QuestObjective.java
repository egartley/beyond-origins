package net.egartley.beyondorigins.ingame;

/**
 * Something the player must do to progress in a {@link Quest}
 */
public class QuestObjective {

    public boolean isComplete;
    public String title;
    public String description;

    public QuestObjective() {
        this("Objective");
    }

    public QuestObjective(String title) {
        this(title, "No description");
    }

    public QuestObjective(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void onComplete() {

    }

    public void complete() {
        boolean first = !isComplete;
        isComplete = true;
        if (first) {
            // only call onComplete() once, so that if complete() is called more than once, onComplete() is not
            onComplete();
        }
    }

    @Override
    public String toString() {
        return title + ": " + description;
    }

}
