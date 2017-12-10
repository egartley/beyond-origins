package net.egartley.beyondorigins.logic.interaction;

/**
 * Extra spacing for boundaries, values can be negative or positive
 * 
 * @author Evan Gartley
 * @see {@link net.egartley.beyondorigins.logic.interaction.Boundary Boundary}
 */
public class BoundaryPadding {

	/**
	 * Extra space added to the top side of the boundary
	 */
	public int	top;
	/**
	 * Extra space added to the left side of the boundary
	 */
	public int	left;
	/**
	 * Extra space added to the bottom side of the boundary
	 */
	public int	bottom;
	/**
	 * Extra space added to the right side of the boundary
	 */
	public int	right;

	/**
	 * Creates new padding with all sides having the same value
	 * 
	 * @param a
	 *            The value to set all sides to
	 */
	public BoundaryPadding(int a) {
		top = a;
		left = a;
		bottom = a;
		right = a;
	}

	/**
	 * Creates new padding with the specified values for each side
	 * 
	 * @param top
	 *            The value for the top side
	 * @param left
	 *            The value for the left side
	 * @param bottom
	 *            The value for the bottom side
	 * @param right
	 *            The value for the right side
	 */
	public BoundaryPadding(int top, int left, int bottom, int right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

}
