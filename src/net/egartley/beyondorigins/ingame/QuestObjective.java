package net.egartley.beyondorigins.ingame;

public class QuestObjective {

    public String title;
    public String description;

    public boolean isComplete;

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

    /*public void checkForCompletion() {

    }*/

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
