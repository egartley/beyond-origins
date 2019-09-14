package net.egartley.gamelib.logic.interaction;

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
     * The boundary's width (includes padding)
     */
    public int width;
    /**
     * The boundary's height (includes padding)
     */
    public int height;
    /**
     * Top side of the boundary, which is the y-coordinate
     */
    public int top;
    /**
     * Right side of the boundary, which is the x-coordinate + width
     */
    public int right;
    /**
     * Bottom side of the boundary, which is the y-coordinate + height
     */
    public int bottom;
    /**
     * Left side of the boundary, which is the x-coordinate
     */
    public int left;
    /**
     * Whether or not the boundary is collided with another
     */
    public boolean isCollided;
    public boolean isVisible = true;

    /**
     * This boundary's padding (extra space added/subtracted from any or all of the four sides)
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
     * Renders the boundary
     *
     * @param graphics Graphics object to use
     */
    public void draw(Graphics graphics) {
        if (isVisible) {
            graphics.setColor(drawColor);
            graphics.drawRect(x, y, width, height);
        }
    }

    /**
     * Updates {@link #x}, {@link #y}, {@link #top}, {@link #left}, {@link #bottom} and {@link #right}
     */
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
