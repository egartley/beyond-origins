package net.egartley.beyondorigins.engine;

import java.util.ArrayList;

public class Mouse {

    private ArrayList<ClickEvent> clickEvents;

    public int x, y;
    public boolean isDragging;

    public Mouse() {
        clickEvents = new ArrayList<>();
    }

    public void onClick(int x, int y) {
        for (ClickEvent event : clickEvents) {
            event.click(x, y);
        }
    }

    public void addClickEvent(ClickEvent event) {
        if (!clickEvents.contains(event)) {
            clickEvents.add(event);
        } else {
            System.out.println("WARNING: Tried to add a click event that was already added");
        }
    }

    public void removeClickEvent(ClickEvent event) {
        if (clickEvents.contains(event)) {
            clickEvents.remove(event);
        } else {
            System.out.println("WARNING: Tried to remove a click event that wasn't previously added");
        }
    }

    public ArrayList<ClickEvent> getClickEvents() {
        return clickEvents;
    }

}
