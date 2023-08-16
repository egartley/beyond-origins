package net.egartley.beyondorigins.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UIElementTest {

    @Test
    void squareConstructor() {
        int size = 200;
        UIElement element = new UIElement(size);
        assertEquals(size, element.width, "Check width");
        assertEquals(size, element.height, "Check height");
    }

    @Test
    void mouseWithinBoundsTrue() {
        UIElement element = new UIElement(200, 200, 50, 50, true);
        assertTrue(element.isMouseWithinBounds(225, 225));
    }

    @Test
    void mouseWithinBoundsFalse() {
        UIElement element = new UIElement(200, 200, 50, 50, true);
        assertFalse(element.isMouseWithinBounds(1, 1));
    }

}