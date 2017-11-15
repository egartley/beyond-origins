package net.egartley.beyondorigins;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Util {

	// Credit: https://stackoverflow.com/a/13605411
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

	public static BufferedImage resized(BufferedImage image, int w, int h) {
		return toBufferedImage(image.getScaledInstance(w, h, Image.SCALE_DEFAULT));
	}

}
