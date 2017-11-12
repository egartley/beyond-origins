package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;

public class SpriteFrame {

	private BufferedImage bufferedImage;

	public SpriteFrame(BufferedImage image) {
		bufferedImage = image;
	}

	public BufferedImage asBufferedImage() {
		return bufferedImage;
	}

}