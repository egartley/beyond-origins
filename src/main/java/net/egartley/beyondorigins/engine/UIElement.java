package net.egartley.beyondorigins.engine;

public class UIElement extends Renderable {

    public boolean isEnabled;

    public UIElement(int size) {
        this(size, size);
    }

    public UIElement(int width, int height) {
        this(0, 0, width, height);
    }

    public UIElement(int x, int y, int width, int height) {
        this(x, y, width, height, true);
    }

    public UIElement(int x, int y, int width, int height, boolean isEnabled) {
        super(x, y, width, height);
        this.isEnabled = isEnabled;
    }

    public boolean isMouseWithinBounds(int mouseX, int mouseY) {
        boolean boundedX = mouseX >= x && mouseX <= x + width;
        boolean boundedY = mouseY >= y && mouseY <= y + height;
        return boundedX && boundedY;
    }

}
