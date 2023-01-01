package net.egartley.beyondorigins.engine.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.controllers.MouseController;
import net.egartley.beyondorigins.engine.input.Mouse;
import net.egartley.beyondorigins.engine.input.MouseClickedEvent;

/**
 * An area on the screen that reacts to mouse interaction
 */
public class ClickableArea {

    private boolean didHover = false;
    private final MouseClickedEvent clickedEvent;

    public int x;
    public int y;
    public int width;
    public int height;
    public boolean isCursorInBounds = false;

    public ClickableArea(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        ClickableArea me = this;
        clickedEvent = new MouseClickedEvent() {
            @Override
            public void onClick(int x, int y) {
                me.checkClick();
            }
        };
        registerClicked();
    }

    private void checkClick() {
        if (isCursorInBounds) {
            onClick();
        }
    }

    /**
     * The mouse was clicked when the cursor was in the area
     */
    public void onClick() {

    }

    /**
     * The cursor is in the area (cursor entered, called again if left and came back)
     */
    public void onHover() {

    }

    public void removeClicked() {
        MouseController.removeMouseClicked(clickedEvent);
    }

    public void registerClicked() {
        MouseController.addMouseClicked(clickedEvent);
    }

    public void tick() {
        isCursorInBounds = Util.isWithinBounds(Mouse.x, Mouse.y, x, y, width, height);
        if (isCursorInBounds && !didHover) {
            onHover();
            didHover = true;
        } else if (!isCursorInBounds && didHover) {
            didHover = false;
        }
    }

}
