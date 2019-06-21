package net.egartley.gamelib.logic.interaction;

/**
 * A numeric offset from the top left (relative origin) of a entity
 */
public class BoundaryOffset {

    int top;
    int bottom;
    int left;
    public int right;

    /**
     * Creates a new boundary offset
     *
     * @param top
     *         Top offset
     * @param bottom
     *         Bottom offset
     * @param left
     *         Left offset
     * @param right
     *         Right offset
     */
    public BoundaryOffset(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

}
