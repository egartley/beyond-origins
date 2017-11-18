package net.egartley.beyondorigins.objects;

import java.awt.Graphics;

public class Animation {

	private byte delay = 0, threshold = 15;
	private int frameIndex;

	public Sprite sprite;
	public SpriteFrame frame;

	public Animation(Sprite s) {
		sprite = s;
		frameIndex = 0;
		frame = sprite.frameCollection.get(frameIndex);
	}

	private SpriteFrame getNextFrame() {
		if (frameIndex + 1 == sprite.frameCollection.size()) {
			frameIndex = 0;
		} else {
			frameIndex++;
		}
		return sprite.frameCollection.get(frameIndex);
	}

	public void render(Graphics graphics, int x, int y) {
		graphics.drawImage(frame.asBufferedImage(), x, y, null);
	}

	public void tick() {
		// called 60 times a second, new frame displayed every threshold/60 seconds
		if (delay < threshold) {
			delay++;
		} else {
			delay = 0;
			frame = getNextFrame();
		}
	}

}
