package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.controllers.MouseController;
import net.egartley.beyondorigins.core.input.Mouse;
import net.egartley.beyondorigins.core.input.MouseClicked;

/**
 * An area on the screen that reacts to mouse interaction
 */
public class ClickableArea {

    private boolean didHover = false;
    private final MouseClicked clicked;

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
        clicked = new MouseClicked() {
            @Override
            public void onClick(int button, int x, int y) {
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
        MouseController.removeMouseClicked(clicked);
    }

    public void registerClicked() {
        MouseController.addMouseClicked(clicked);
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
