package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.egartley.beyondorigins.Util;

public class Sprite {

	private int uuid;

	public BufferedImage sheetImage;
	public ArrayList<AnimationFrame> frameCollection = new ArrayList<AnimationFrame>();
	public int frameWidth, frameHeight;
	public short currentFrameIndex = 0;

	public Sprite(BufferedImage image, int size) {
		sheetImage = image;
		frameWidth = size;
		frameHeight = size;
		uuid = Util.randomInt(9999, 1000, true);
	}

	public Sprite(BufferedImage image, int width, int height) {
		sheetImage = image;
		frameWidth = width;
		frameHeight = height;
		uuid = Util.randomInt(9999, 1000, true);
	}

	public AnimationFrame getFrameAt(int index) {
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
			frameCollection.add(new AnimationFrame(this, 0));
			return;
		}
		for (int i = 0; i < numberOfFrames; i++) {
			frameCollection.add(new AnimationFrame(this, i));
		}
	}

	@Override
	public String toString() {
		return Integer.toHexString(uuid);
	}

}
