package net.egartley.beyondorigins.objects;

import java.awt.Graphics;

public class Animation {

	private byte delay = 0, threshold = 10;
	private int frameIndex, startIndex;
	public boolean isStopped;
	private boolean setStopFrame;

	public Sprite sprite;
	public SpriteFrame currentFrame;

	public Animation(Sprite s) {
		sprite = s;
		frameIndex = 0;
		startIndex = 0;
		currentFrame = sprite.frameCollection.get(0);
	}

	public Animation(Sprite s, int startIndex) {
		sprite = s;
		frameIndex = startIndex;
		this.startIndex = startIndex;
		currentFrame = sprite.frameCollection.get(startIndex);
	}

	private SpriteFrame nextFrame() {
		if (frameIndex + 1 == sprite.frameCollection.size()) {
			frameIndex = 0;
		} else {
			frameIndex++;
		}
		return sprite.frameCollection.get(frameIndex);
	}
	
	public void setThreshold(byte t) {
		threshold = t;
	}

	public void resume() {
		// Debug.out("Resumed (" + sprite + ")");
		isStopped = false;
	}

	public void pause() {
		// Debug.out("Paused (" + sprite + ")");
		isStopped = true;
	}
	
	public void stop() {
		// Debug.out("Stopped (" + sprite + ")");
		isStopped = true;
		setStopFrame = false;
	}

	public void restart() {
		// Debug.out("Restarted (" + sprite + ")");
		frameIndex = startIndex;
		delay = 0;
		isStopped = false;
	}

	public void render(Graphics graphics, int x, int y) {
		graphics.drawImage(currentFrame.asBufferedImage(), x, y, null);
	}

	public void tick() {
		// called 60 times a second, new frame displayed every threshold/60 seconds
		if (!isStopped) {
			if (delay < threshold) {
				delay++;
			} else {
				delay = 0;
				currentFrame = nextFrame();
			}
		} else {
			if (!setStopFrame) {
				currentFrame = sprite.frameCollection.get(startIndex);
				setStopFrame = true;
			}
		}
	}

}
