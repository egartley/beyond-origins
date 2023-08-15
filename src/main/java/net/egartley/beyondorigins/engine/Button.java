package net.egartley.beyondorigins.engine;

import net.egartley.beyondorigins.Game;

public class Button extends UIElement implements Tickable {

    public boolean isHovering;

    public ClickEvent clickEvent;

    public Button(int width, int height) {
        this(0, 0, width, height);
    }

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
        clickEvent = new ClickEvent() {
            @Override
            public void click(int x, int y) {
                checkClick(x, y);
            }
        };
    }

    @Override
    public void tick() {
        isHovering = isEnabled && isMouseWithinBounds();
    }

    public void registerClickEvent() {
        Game.mouse.addClickEvent(clickEvent);
    }

    public void removeClickEvent() {
        Game.mouse.removeClickEvent(clickEvent);
    }

    private void checkClick(int x, int y) {
        if (isMouseWithinBounds(x, y)) {
            onClick();
        }
    }

    public void onClick() {

    }

}
