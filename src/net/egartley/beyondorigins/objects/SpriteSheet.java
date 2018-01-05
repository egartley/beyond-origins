package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * An image that contains multiple sprites, each represented by a row, or
 * "strip" that is specified in the constructor
 * 
 * @author Evan Gartley
 * @see Sprite
 */
public class SpriteSheet {

	private BufferedImage sheet;
	private ArrayList<Sprite> spriteCollection;

	public int spriteWidth;
	public int spriteHeight;
	public int strips;
	public int stripWidth;
	public short frames;

	/**
	 * Creates a new sprite strip, then loads its sprites
	 * 
	 * @param image
	 *            BufferedImage that represents the entire strip
	 * @param width
	 *            Width of each sprite/frame (same for all of them)
	 * @param height
	 *            Height of each sprite/frame (same for all of them)
	 * @param rows
	 *            Number of rows, or "strips", of sprites that are in the strip
	 * @param frames
	 *            Number of frames in each row, or "strip" (same for entire strip)
	 */
	public SpriteSheet(BufferedImage image, int width, int height, int rows, short frames) {
		sheet = image;
		spriteWidth = width;
		spriteHeight = height;
		strips = rows;
		this.frames = frames;
		stripWidth = spriteWidth * frames;
		loadAllSprites();
	}

	/**
	 * Sets the strip's image
	 * 
	 * @param image
	 *            The BufferedImage to replace the strip with
	 */
	public void setSheetImage(BufferedImage image) {
		sheet = image;
	}

	/**
	 * Builds the sprite collection from the strip image (should have already been
	 * set)
	 */
	private void loadAllSprites() {
		// clear sprite collection if previously built
		spriteCollection = new ArrayList<Sprite>(strips);
		for (int i = 0; i < strips; i++) {
			// get each row/strip as a sprite
			Sprite s = new Sprite(getStripAsBufferedImage(i), spriteWidth, spriteHeight);
			s.setFrames(frames);
			// add to collection
			spriteCollection.add(s);
		}
	}

	/**
	 * Returns the "strip" at the given row index
	 * 
	 * @param rowIndex
	 *            The row number, or index, of the "strip" to return in the strip
	 * @return The specified "strip" as a BufferedImage
	 */
	public BufferedImage getStripAsBufferedImage(int rowIndex) {
		return sheet.getSubimage(0, rowIndex * spriteHeight, stripWidth, spriteHeight);
	}

	/**
	 * Returns the entire sprite strip
	 * 
	 * @return The sprite strip as a BufferedImage object
	 */
	public BufferedImage asBufferedImage() {
		return sheet;
	}

	/**
	 * @param index
	 *            Index of the sprite to get within the strip's sprite collection
	 * @return The sprite at the given index in the strip's sprite collection
	 */
	public Sprite getSpriteAt(int index) {
		return spriteCollection.get(index);
	}

	/**
	 * Returns the strip's sprite collection
	 * 
	 * @return An array list of sprites within the strip
	 */
	public ArrayList<Sprite> getSpriteCollection() {
		return spriteCollection;
	}

}
