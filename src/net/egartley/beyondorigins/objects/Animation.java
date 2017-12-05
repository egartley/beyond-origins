package net.egartley.beyondorigins.objects;

import java.awt.Graphics;

/**
 * Represents a collection of <code>AnimationFrame</code>s used for animating,
 * and the control thereof, a <code>Sprite</code>
 * 
 * @author Evan Gartley
 *
 * @see AnimationFrame
 */
public class Animation {

	private byte			delay		= 0;
	private byte			threshold	= 10;
	private int				frameIndex, startIndex;
	public boolean			isStopped;
	private boolean			setStopFrame;

	public Sprite			sprite;
	public AnimationFrame	frame, startFrame;

	/**
	 * Creates a new animation
	 * 
	 * @param s
	 *            Sprite to animate (must have at least one frame)
	 */
	public Animation(Sprite s) {
		sprite = s;
		frameIndex = 0;
		startIndex = 0;
		startFrame = sprite.frameCollection.get(startIndex);
		frame = startFrame;
	}

	/**
	 * Creates a new animation, starting at the given index
	 * 
	 * @param s
	 *            Sprite to animate (at least one frame)
	 * @param startIndex
	 *            The index of the frame to start at (from
	 *            {@link Sprite#frameCollection frameCollection})
	 */
	public Animation(Sprite s, int start) {
		sprite = s;
		frameIndex = start;
		startIndex = frameIndex;
		startFrame = sprite.frameCollection.get(startIndex);
		frame = startFrame;
	}

	private AnimationFrame nextFrame()
	{
		if (frameIndex + 1 == sprite.frameCollection.size()) {
			frameIndex = 0;
		}
		else {
			frameIndex++;
		}
		return sprite.frameCollection.get(frameIndex);
	}

	/**
	 * <p>
	 * Sets the animation's threshold, or interval, for when to go to the next frame
	 * </p>
	 * <p>
	 * The tick method should be called roughly 60 times per second, therefore each
	 * frame will be displayed for about <b>threshold ÷ 60</b> seconds
	 * </p>
	 * 
	 * @param t
	 *            The new value for <code>threshold</code>
	 */
	public void setThreshold(int t)
	{
		if (t > 127 || t < -128) {
			return; // out of range for a byte
		}
		threshold = (byte) t;
	}

	/**
	 * Resume the animation. Does nothing if already running
	 */
	public void resume()
	{
		isStopped = false;
	}

	/**
	 * Pauses the animation. Does nothing if already paused
	 */
	public void pause()
	{
		isStopped = true;
	}

	/**
	 * Stops the animation, and resets the displayed frame to the starting frame.
	 * Does nothing if already stopped
	 */
	public void stop()
	{
		isStopped = true;
		setStopFrame = false;
	}

	/**
	 * Restarts the animation. If already running, the animation will start over.
	 */
	public void restart()
	{
		frameIndex = startIndex;
		delay = 0;
		isStopped = false;
	}

	/**
	 * Renders the current <code>AnimationFrame</code> ({@link Animation#frame
	 * frame})
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object
	 * @param x
	 *            The x-axis coordinate
	 * @param y
	 *            The y-axis coordinate
	 * 
	 * @see #frame
	 * @see AnimationFrame
	 */
	public void render(Graphics graphics, int x, int y)
	{
		graphics.drawImage(frame.asBufferedImage(), x, y, null);
	}

	/**
	 * Should be called 60 times per second in a tick thread
	 */
	public void tick()
	{
		if (!isStopped) {
			if (delay < threshold) {
				delay++;
			}
			else {
				delay = 0;
				frame = nextFrame();
			}
		}
		else {
			if (!setStopFrame) {
				frame = startFrame;
				setStopFrame = true;
			}
		}
	}

}
