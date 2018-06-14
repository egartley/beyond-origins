package net.egartley.beyondorigins.logic.interaction;

import net.egartley.beyondorigins.Game;

import java.awt.*;

/**
 * An imaginary boundary, or border, with a width, height and coordinates
 */
public abstract class Boundary {

    /**
     * Absolute x-coordinate
     */
    public int x;
    /**
     * Absolute y-coordinate
     */
    public int y;
    int horizontalOffset;
    int verticalOffset;
    /**
     * The boundary's width (calculated with padding)
     */
    public int width;
    /**
     * The boundary's height (calculated with padding)
     */
    public int height;
    /**
     * Top side of the boundary, which is its y-coordinate
     */
    public int top;
    /**
     * Right side of the boundary, which is its x-coordinate
     */
    public int right;
    /**
     * Bottom side of the boundary, which is its y-coordinate
     */
    public int bottom;
    /**
     * Left side of the boundary, which is its x-coordinate
     */
    public int left;
    /**
     * Whether or not the boundary is collided with another
     */
    public boolean isCollided;

    /**
     * This boundary's padding (extra space added/subtracted from any or all of the
     * four sides)
     */
    BoundaryPadding padding;
    /**
     * This boundary's offset (change in its coordinate)
     */
    BoundaryOffset offset;
    /**
     * The current color that is being used while rendering
     */
    public Color drawColor;

    /**
     * Uses {@link java.awt.Graphics#drawRect(int, int, int, int) drawRect} to
     * render this boundary (only if {@link net.egartley.beyondorigins.Game#debug
     * Game.debug} is enabled)
     *
     * @param graphics Graphics object to use
     */
    public void draw(Graphics graphics) {
        if (Game.debug) {
            graphics.setColor(drawColor);
            graphics.drawRect(x, y, width, height);
        }
    }

    /**
     * Update {@link #x}, {@link #y}, {@link #top}, {@link #left}, {@link #bottom}
     * and {@link #right}
     */
    public void tick() {
        top = y;
        bottom = top + height;
        left = x;
        right = left + width;
    }

    /**
     * @return The boundary as a {@link java.awt.Rectangle Rectangle} object
     */
    public Rectangle asRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public String toString() {
        return "Boundary#" + hashCode();
    }

}
