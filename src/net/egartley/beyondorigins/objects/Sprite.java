package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite {

	public BufferedImage sheetImage;
	public ArrayList<SpriteFrame> frames = new ArrayList<SpriteFrame>();
	public boolean isAnimated, isSquareFrames;
	public int frameWidth, frameHeight, currentFrame = 0;

	public Sprite(BufferedImage image, int size) {
		this.sheetImage = image;
		frameWidth = size;
		frameHeight = size;
		isSquareFrames = frameWidth == frameHeight;
	}

	public Sprite(BufferedImage image, int size, boolean animated) {
		this.sheetImage = image;
		frameWidth = size;
		frameHeight = size;
		isSquareFrames = frameWidth == frameHeight;
		isAnimated = animated;
	}

	public Sprite(BufferedImage image, int width, int height) {
		this.sheetImage = image;
		frameWidth = width;
		frameHeight = height;
		isSquareFrames = frameWidth == frameHeight;
	}

	public Sprite(BufferedImage image, int width, int height, boolean animated) {
		this.sheetImage = image;
		frameWidth = width;
		frameHeight = height;
		isSquareFrames = frameWidth == frameHeight;
		isAnimated = animated;
	}

	public SpriteFrame getFrame(int index) {
		if (index >= frames.size())
			return null;
		return frames.get(index);
	}
	
	public BufferedImage getCurrentFrameAsBufferedImage() {
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
