package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite {

	public BufferedImage imageStrip;
	public ArrayList<SpriteFrame> frames = new ArrayList<SpriteFrame>();
	public boolean isAnimated, squareFrames;
	public int frameWidth, frameHeight, currentFrame = 0;

	public Sprite(BufferedImage image, int size) {
		this.imageStrip = image;
		frameWidth = size;
		frameHeight = size;
		squareFrames = frameWidth == frameHeight;
		// setFrames(numberOfFrames);
	}

	public Sprite(BufferedImage image, int size, boolean animated) {
		this.imageStrip = image;
		frameWidth = size;
		frameHeight = size;
		squareFrames = frameWidth == frameHeight;
		isAnimated = animated;
		// setFrames(numberOfFrames);
	}

	public Sprite(BufferedImage image, int width, int height) {
		this.imageStrip = image;
		frameWidth = width;
		frameHeight = height;
		squareFrames = frameWidth == frameHeight;
		// setFrames(numberOfFrames);
	}

	public Sprite(BufferedImage image, int width, int height, boolean animated) {
		this.imageStrip = image;
		frameWidth = width;
		frameHeight = height;
		squareFrames = frameWidth == frameHeight;
		isAnimated = animated;
		// setFrames(numberOfFrames);
	}

	public SpriteFrame getFrame(int index) {
		if (index >= frames.size())
			return null;
		return frames.get(index);
	}
	
	public BufferedImage getCurrentFrameAsBufferedImage() {
		System.out.println(currentFrame);
		return frames.get(currentFrame).asBufferedImage();
	}

	public void setFrames(int number) {
		frames.clear();
		if (number == 1) {
			frames.add(new SpriteFrame(this, 0));
			return;
		}
		for (int i = 0; i < number; i++) {
			frames.add(new SpriteFrame(this, i));
		}
	}

}
