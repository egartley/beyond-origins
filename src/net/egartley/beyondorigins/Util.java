package net.egartley.beyondorigins;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Miscellaneous methods that don't fit into a particular class or object
 * 
 * @author Evan Gartley
 */
public class Util {

	// https://stackoverflow.com/a/13605411
	private static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
	}

	/**
	 * Returns a resized image of the original with the supplied width and height
	 * 
	 * @param image
	 *            The original image to resize (won't be changed)
	 * @param w
	 *            New width to resize to
	 * @param h
	 *            New height to resize to
	 * @return A resized version of the given buffered image
	 */
	public static BufferedImage resized(BufferedImage image, int w, int h) {
		return toBufferedImage(image.getScaledInstance(w, h, Image.SCALE_DEFAULT));
	}

	/**
	 * Returns a random integer between the supplied maximum and minimum
	 * 
	 * @param max
	 *            The maximum value the random integer could be
	 * @param min
	 *            The minimum value the random integer could be
	 * @return A randon integer that is between the given maximum and minimum
	 */
	public static int randomInt(int max, int min) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	/**
	 * Returns a random integer between the supplied maximum and minimum
	 * 
	 * @param max
	 *            The maximum value the random integer could be
	 * @param min
	 *            The minimum value the random integer could be
	 * @param inclusive
	 *            Whether or not the include the maximum as a possible value
	 * @return A randon integer that is between the given maximum and minimum
	 */
	public static int randomInt(int max, int min, boolean inclusive) {
		if (inclusive) {
			return randomInt(max + 1, min);
		} else {
			return randomInt(max, min);
		}
	}

}
