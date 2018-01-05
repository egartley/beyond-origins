package net.egartley.beyondorigins.logic.math;

public class Calculate {

	/**
	 * Returns the "center" of b from a
	 * 
	 * @param a
	 *            Base x-axis or y-axis coordinate
	 * @param b
	 *            Width or height
	 * @return a + (b / 2)
	 */
	private static int getCenter(int a, int b) {
		return a + (b / 2);
	}

	/**
	 * Returns the vertical "center"
	 * 
	 * @param y
	 *            The base y-axis coordinate
	 * @param height
	 *            The height to use
	 * @return y + (height / 2)
	 */
	public static int verticalCenter(int y, int height) {
		return getCenter(y, height);
	}

	/**
	 * Returns the horizontal "center"
	 * 
	 * @param x
	 *            The base x-axis coordinate
	 * @param width
	 *            The width to use
	 * @return x + (width / 2)
	 */
	public static int horizontalCenter(int x, int width) {
		return getCenter(x, width);
	}

}
