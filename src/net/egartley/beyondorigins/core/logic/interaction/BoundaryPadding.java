package net.egartley.beyondorigins.core.logic.interaction;

/**
 * Extra spacing for boundaries, either negative or positive
 */
public class BoundaryPadding {

    public int top;
    public int left;
    public int right;
    public int bottom;

    /**
     * Creates a new boundary padding with the same value for all sides
     */
    public BoundaryPadding(int padding) {
        top = padding;
        left = padding;
        bottom = padding;
        right = padding;
    }

    public BoundaryPadding(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

}
