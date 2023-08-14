package net.egartley.beyondorigins.engine;

public class Button extends UIElement implements Tickable {

    public boolean isHovering;

    public Button(int width, int height) {
        this(0, 0, width, height);
    }

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void tick() {
        isHovering = isEnabled && isMouseWithinBounds();
    }

}
