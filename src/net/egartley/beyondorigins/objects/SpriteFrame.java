package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;

public class SpriteFrame {

	private Sprite parent;
	private BufferedImage frameImage;

	public int index;

	public SpriteFrame(Sprite parent, int index) {
		this.parent = parent;
		this.index = index;
		try {
			frameImage = parent.imageStrip.getSubimage(index * parent.frameWidth, 0, parent.frameWidth,
					parent.frameHeight);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Sprite getParent() {
		return parent;
	}

	public BufferedImage asBufferedImage() {
		return frameImage;
	}

}