package net.egartley.beyondorigins.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MouseTest {

    @Test
    void addClickEvent() {
        Mouse mouse = new Mouse();
        ClickEvent event = new ClickEvent();
        mouse.addClickEvent(event);
        assertEquals(event, mouse.getClickEvents().get(0));
        assertEquals(1, mouse.getClickEvents().size());
    }

    @Test
    void removeClickEvent() {
        Mouse mouse = new Mouse();
        ClickEvent event = new ClickEvent();
        mouse.getClickEvents().add(event);
        mouse.removeClickEvent(event);
        assertEquals(0, mouse.getClickEvents().size());
    }

}