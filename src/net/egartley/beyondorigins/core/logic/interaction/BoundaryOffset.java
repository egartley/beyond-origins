package net.egartley.beyondorigins.core.logic.interaction;

/**
 * A numeric offset from the top left (relative origin) of an entity
 */
public class BoundaryOffset {

    public int top;
    public int left;
    public int right;
    public int bottom;

    public BoundaryOffset(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

}
