package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite {

	public BufferedImage sheetImage;
	public ArrayList<SpriteFrame> frameCollection = new ArrayList<SpriteFrame>();
	public boolean isAnimated, isSquareFrames;
	public int frameWidth, frameHeight, currentFrameIndex = 0;

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

	public SpriteFrame getFrameAt(int index) {
		if (index >= frameCollection.size())
			return null;
		return frameCollection.get(index);
	}
	
	public BufferedImage getCurrentFrameAsBufferedImage() {
		return frameCollection.get(currentFrameIndex).asBufferedImage();
	}

	public void setFrames(int numberOfFrames) {
		frameCollection.clear();
		if (numberOfFrames == 1) {
			frameCollection.add(new SpriteFrame(this, 0));
			return;
		}
		for (int i = 0; i < numberOfFrames; i++) {
			frameCollection.add(new SpriteFrame(this, i));
		}
	}

}
