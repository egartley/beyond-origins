package net.egartley.beyondorigins.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MouseTest {

    @Test
    void addSingleClickEvent() {
        Mouse mouse = new Mouse();
        ClickEvent event = new ClickEvent();

        mouse.addClickEvent(event);

        assertTrue(mouse.getClickEvents().contains(event), "clickEvents contains test event");
        assertEquals(1, mouse.getClickEvents().size(), "Size of clickEvents is 1");
    }

    @Test
    void removeSingleClickEvent() {
        Mouse mouse = new Mouse();
        ClickEvent event = new ClickEvent();

        mouse.getClickEvents().add(event);
        mouse.removeClickEvent(event);

        assertFalse(mouse.getClickEvents().contains(event), "clickEvents doesn't contain test event");
        assertEquals(0, mouse.getClickEvents().size(), "clickEvents is empty");
    }

    @Test
    void addMultipleClickEvents() {
        Mouse mouse = new Mouse();
        ClickEvent event = new ClickEvent();
        ClickEvent event2 = new ClickEvent();
        ClickEvent event3 = new ClickEvent();

        mouse.addClickEvent(event);
        mouse.addClickEvent(event2);
        mouse.addClickEvent(event3);

        assertTrue(mouse.getClickEvents().contains(event), "clickEvents contains test event");
        assertTrue(mouse.getClickEvents().contains(event2), "clickEvents contains test event2");
        assertTrue(mouse.getClickEvents().contains(event3), "clickEvents contains test event3");
        assertEquals(3, mouse.getClickEvents().size(), "Size of clickEvents is 3");
    }

    @Test
    void removeMultipleClickEvents() {
        Mouse mouse = new Mouse();
        ClickEvent event = new ClickEvent();
        ClickEvent event2 = new ClickEvent();
        ClickEvent event3 = new ClickEvent();

        mouse.getClickEvents().add(event);
        mouse.getClickEvents().add(event2);
        mouse.getClickEvents().add(event3);
        mouse.removeClickEvent(event);
        mouse.removeClickEvent(event2);
        mouse.removeClickEvent(event3);

        assertFalse(mouse.getClickEvents().contains(event), "clickEvents doesn't contain test event");
        assertFalse(mouse.getClickEvents().contains(event2), "clickEvents doesn't contain test event2");
        assertFalse(mouse.getClickEvents().contains(event3), "clickEvents doesn't contain test event3");
        assertEquals(0, mouse.getClickEvents().size(), "clickEvents is empty");
    }

}