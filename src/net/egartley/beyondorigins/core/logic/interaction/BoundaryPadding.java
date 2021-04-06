package net.egartley.beyondorigins.core.logic.interaction;

/**
 * Extra spacing for boundaries, values can be negative or positive
 */
public class BoundaryPadding {

    public int top;
    public int left;
    public int right;
    public int bottom;

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
