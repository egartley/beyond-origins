package net.egartley.beyondorigins.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UIElementTest {

    @Test
    void squareConstructor() {
        UIElement element = new UIElement(200);
        assertEquals(200, element.width);
        assertEquals(200, element.height);
    }

    @Test
    void isMouseWithinBounds() {
        UIElement element = new UIElement(200, 200, 50, 50, true);
        assertTrue(element.isMouseWithinBounds(225, 225));
        assertFalse(element.isMouseWithinBounds(1, 1));
    }

}