package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;

/**
 * A "tile" that is rendered within a map sector
 * 
 * @author Evan Gartley
 */
public class MapTile {

	/**
	 * This tile as a {@link BufferedImage} (used while rendering)
	 */
	public BufferedImage bufferedImage;
	public int width, height;
	public boolean isTraversable;

	/**
	 * Creates a new map tile with the provided {@link BufferedImage}
	 * 
	 * @param image
	 *            The tile's image
	 */
	public MapTile(BufferedImage image) {
		bufferedImage = image;
		width = image.getWidth();
		height = image.getHeight();
	}

}
