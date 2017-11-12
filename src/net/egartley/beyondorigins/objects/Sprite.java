package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Sprite {

	private BufferedImage fullBufferedImage;
	private ArrayList<SpriteFrame> frames;
	private boolean isAnimated;

	public int frameWidth, frameHeight;
	public boolean squareFrames;

	public Sprite(BufferedImage fullImage, int size, int numberOfFrames) {
		fullBufferedImage = fullImage;
		frameWidth = size;
		frameHeight = size;
		squareFrames = frameWidth == frameHeight;
		setFrames(numberOfFrames);
	}

	public Sprite(BufferedImage fullImage, int size, int numberOfFrames, boolean animated) {
		fullBufferedImage = fullImage;
		frameWidth = size;
		frameHeight = size;
		squareFrames = frameWidth == frameHeight;
		isAnimated = animated;
		setFrames(numberOfFrames);
	}

	public Sprite(BufferedImage fullImage, int width, int height, int numberOfFrames) {
		fullBufferedImage = fullImage;
		frameWidth = width;
		frameHeight = height;
		squareFrames = frameWidth == frameHeight;
		setFrames(numberOfFrames);
	}

	public Sprite(BufferedImage fullImage, int width, int height, int numberOfFrames, boolean animated) {
		fullBufferedImage = fullImage;
		frameWidth = width;
		frameHeight = height;
		squareFrames = frameWidth == frameHeight;
		isAnimated = animated;
		setFrames(numberOfFrames);
	}

	public BufferedImage asBufferedImage() {
		return fullBufferedImage;
	}

	public ArrayList<SpriteFrame> getFrames() {
		return frames;
	}

	public SpriteFrame getFrame(int index) {
		if (index >= frames.size())
			return null;
		return frames.get(index);
	}

	public boolean isAnimated() {
		return isAnimated;
	}

	private void setFrames(int number) {
		if (number == 1) {
			frames.add(new SpriteFrame(fullBufferedImage.getSubimage(0, 0, frameWidth, frameHeight)));
			return;
		}
		for (int i = 0; i < number; i++) {
			frames.add(new SpriteFrame(fullBufferedImage.getSubimage(i * frameWidth, 0, frameWidth, frameHeight)));
		}
	}

}
