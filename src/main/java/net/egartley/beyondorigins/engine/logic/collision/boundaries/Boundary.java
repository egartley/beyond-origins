package net.egartley.beyondorigins.engine.logic.collision.boundaries;

import net.egartley.beyondorigins.engine.interfaces.Renderable;
import net.egartley.beyondorigins.engine.interfaces.Tickable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.*;

/**
 * A specified area on the screen
 */
public abstract class Boundary implements Tickable, Renderable {

    protected int verticalOffset;
    protected int horizontalOffset;
    protected BoundaryOffset offset;
    protected BoundaryPadding padding;

    public int x, y;
    public int top;
    public int left;
    public int right;
    public int width;
    public int height;
    public int bottom;
    public boolean isCollided;
    public boolean isVisible = true;
    public Color drawColor;

    @Override
    public void render(Graphics graphics) {
        if (isVisible) {
            graphics.setColor(drawColor);
            graphics.drawRect(x, y, width, height);
        }
    }

    @Override
    public void tick() {
        top = y;
        bottom = top + height;
        left = x;
        right = left + width;
    }

    public Rectangle asRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public String toString() {
        return "Boundary#" + hashCode();
    }

}
