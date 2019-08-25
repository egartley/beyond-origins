package net.egartley.gamelib.abstracts;

import java.awt.*;

/**
 * Makes something able to rendered on the screen at a specified position
 */
public abstract class Renderable {

    /**
     * The x-coordinate to render at
     */
    private int x;
    /**
     * The y-coordinate to render at
     */
    private int y;

    protected abstract void render(Graphics graphics);

    /**
     * Sets both the {@link #x} and {@link #y} coordinates
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     */
    public void setPosition(int x, int y) {
        x(x);
        y(y);
    }

    /**
     * Sets the x-coordinate ({@link #x})
     *
     * @param x The new x-coordinate
     */
    public void x(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate ({@link #y})
     *
     * @param y The new y-coordinate
     */
    public void y(int y) {
        this.y = y;
    }

    /**
     * Gets the x-coordinate ({@link #x})
     *
     * @return The x-coordinate
     */
    public int x() {
        return x;
    }

    /**
     * Gets the y-coordinate ({@link #y})
     *
     * @return The y-coordinate
     */
    public int y() {
        return y;
    }

}
