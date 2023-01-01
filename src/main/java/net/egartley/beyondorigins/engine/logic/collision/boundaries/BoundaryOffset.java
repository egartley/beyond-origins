package net.egartley.beyondorigins.engine.logic.collision.boundaries;

/**
 * A numeric offset from the top left (relative origin) of a boundary
 */
public class BoundaryOffset {

    public int top;
    public int left;
    public int right;
    public int bottom;

    /**
     * Creates a new boundary offset with the same value for all sides
     */
    public BoundaryOffset(int value) {
        this(value, value, value, value);
    }

    public BoundaryOffset(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

}
