package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.gamelib.abstracts.Renderable;
import net.egartley.gamelib.interfaces.Tickable;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class UIElement extends Renderable implements Tickable {

    public int width;
    public int height;
    public boolean isEnabled;
    public BufferedImage image;

    public UIElement(BufferedImage image) {
        this(image, true);
    }

    public UIElement(BufferedImage image, boolean enabled) {
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

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        if (image != null) {
            graphics.drawImage(image, x(), y(), null);
        }
    }

    public boolean isClickInBounds(int cx, int cy) {
        return Util.isWithinBounds(cx, cy, x(), y(), width, height);
    }

}
