package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A collection of images that can used while rendering entities
 * 
 * @author Evan Gartley
 * @see SpriteSheet
 * @see Entity
 */
public class Sprite {

	/**
	 * The image that contains all of the sprite's possible frames
	 * 
	 * @see AnimationFrame
	 * @see #frames
	 */
	public BufferedImage				sheet;
	/**
	 * All of the possible frames that the sprite could use, which are derived from {@link #sheet}
	 * 
	 * @see AnimatedEntity
	 * @see StaticEntity
	 */
	public ArrayList<AnimationFrame>	frames		= new ArrayList<AnimationFrame>();
	/**
	 * The width in pixels for each frame
	 */
	public int							frameWidth;
	/**
	 * The height in pixels for each frame
	 */
	public int							frameHeight;
	public short						currentFrameIndex	= 0;

	public Sprite(BufferedImage image, int size) {
		this(image, size, size);
	}

	public Sprite(BufferedImage image, int width, int height) {
		sheet = image;
		frameWidth = width;
		frameHeight = height;
	}

	/**
	 * Returns the frame at the given index (from {@link #frames})
	 * 
	 * @param index
	 *            Index of the frame to return
	 * @return {@link AnimationFrame}
	 */
	public AnimationFrame getFrameAt(int index)
	{
		if (index >= frames.size())
			return null;
		return frames.get(index);
	}

	/**
	 * Returns the current frame
	 * ({@link #frames}[{@link #currentFrameIndex}]) as a buffered image
	 * 
	 * @return {@link java.awt.image.BufferedImage BufferedImage}
	 */
	public BufferedImage getCurrentFrameAsBufferedImage()
	{
		return frames.get(currentFrameIndex).asBufferedImage();
	}

	/**
	 * Resets {@link #frames} and re-loads it with the given number of
	 * frames
	 * 
	 * @param numberOfFrames
	 *            How many frames to use (must be at least 1)
	 * 
	 * @see AnimationFrame
	 */
	public void setFrames(int numberOfFrames)
	{
		frames.clear();
		if (numberOfFrames == 1) {
			frames.add(new AnimationFrame(this, 0));
			return;
		}
		for (int i = 0; i < numberOfFrames; i++) {
			frames.add(new AnimationFrame(this, i));
		}
	}

}
