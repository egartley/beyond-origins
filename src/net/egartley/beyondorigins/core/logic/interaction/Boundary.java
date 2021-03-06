package net.egartley.beyondorigins.core.logic.interaction;

import net.egartley.beyondorigins.core.abstracts.Renderable;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.*;

public abstract class Boundary extends Renderable implements Tickable {

    protected int horizontalOffset;
    protected int verticalOffset;
    protected BoundaryPadding padding;
    protected BoundaryOffset offset;

    public int x;
    public int y;
    public int top, bottom;
    public int left, right;
    public int width, height;
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
