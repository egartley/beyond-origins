package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.controllers.MouseController;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.input.MouseClicked;

public class ClickableArea {

    private boolean didHover = false;
    private final MouseClicked clicked;

    public int x, y, width, height;
    public boolean mouseWithinBounds = false;

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
        if (mouseWithinBounds) {
            onClick();
        }
    }

    public void onClick() {

    }

    public void onHover() {

    }

    public void removeClicked() {
        MouseController.removeMouseClicked(clicked);
    }

    public void registerClicked() {
        MouseController.addMouseClicked(clicked);
    }

    public void tick() {
        mouseWithinBounds = Util.isWithinBounds(Mouse.x, Mouse.y, x, y, width, height);
        if (mouseWithinBounds && !didHover) {
            onHover();
            didHover = true;
        } else if (!mouseWithinBounds && didHover) {
            didHover = false;
        }
    }

}
