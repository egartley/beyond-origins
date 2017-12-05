package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;

/**
 * A single frame within an {@link Animation}
 * 
 * @author Evan Gartley
 * @see Animation
 */
public class AnimationFrame {

	private Sprite			parent;
	private BufferedImage	bufferedImage;

	public int				index;

	/**
	 * Creates a new frame for use in an {@link Animation}
	 * 
	 * @param parent
	 * @param index
	 * @see Animation
	 */
	public AnimationFrame(Sprite parent, int index) {
		this.parent = parent;
		this.index = index;
		try {
			bufferedImage = parent.sheetImage.getSubimage(index * parent.frameWidth, 0, parent.frameWidth,
					parent.frameHeight);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns this frame's "parent" sprite ({@link AnimationFrame#parent})
	 * 
	 * @return {@link Sprite}
	 */
	public Sprite getParent()
	{
		return parent;
	}

	/**
	 * Returns this frame as a {@link BufferedImage}
	 * 
	 * @return {@link BufferedImage}
	 */
	public BufferedImage asBufferedImage()
	{
		return bufferedImage;
	}

}
