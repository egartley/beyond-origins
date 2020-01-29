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
        isComplete = true;
        onComplete();
    }

    @Override
    public String toString() {
        return title + ": " + description;
    }

}
