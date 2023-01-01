package net.egartley.beyondorigins.engine.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.interfaces.Renderable;
import net.egartley.beyondorigins.engine.interfaces.Tickable;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class UIElement implements Tickable, Renderable {

    public int x, y;
    public int width;
    public int height;
    public boolean isEnabled;
    public Image image;

    public UIElement() {

    }

    public UIElement(Image image) {
        this(image, true);
    }

    public UIElement(Image image, boolean enabled) {
        this(image.getWidth(), image.getHeight(), enabled);
        this.image = image;
    }

    public UIElement(int size) {
        this(size, size);
    }

    public UIElement(int width, int height) {
        this(width, height, true);
    }

    public UIElement(int width, int height, boolean enabled) {
        this.width = width;
        this.height = height;
        this.isEnabled = enabled;
    }

    public boolean isClickInBounds(int cx, int cy) {
        return Util.isWithinBounds(cx, cy, x, y, width, height);
    }

    @Override
    public void render(Graphics graphics) {
        if (image != null) {
            graphics.drawImage(image, x, y);
        }
    }

}
