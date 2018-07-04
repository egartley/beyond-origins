package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A row, or "strip", of individual frames of an entity or object
 *
 * @see SpriteSheet
 * @see Entity
 */
public class Sprite {

    /**
     * The image that contains all of the sprite's possible frames
     *
     * @see AnimationFrame
     * @see #frameCollection
     */
    BufferedImage strip;
    /**
     * All of the possible frameCollection that the sprite could use, which are derived from {@link #strip}
     *
     * @see AnimatedEntity
     * @see StaticEntity
     */
    ArrayList<AnimationFrame> frameCollection = new ArrayList<>();
    /**
     * The width in pixels for each frame
     */
    public int width;
    /**
     * The height in pixels for each frame
     */
    public int height;
    private short currentFrameIndex = 0;

    public Sprite(BufferedImage image, int width, int height) {
        strip = image;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the frame at the given index (from {@link #frameCollection})
     *
     * @param index
     *         Index of the frame to return
     *
     * @return {@link AnimationFrame}
     */
    public AnimationFrame getFrameAt(int index) {
        if (index >= frameCollection.size())
            return null;
        return frameCollection.get(index);
    }

    /**
     * Returns the current frame ({@link #frameCollection}[{@link #currentFrameIndex}]) as a buffered image
     *
     * @return {@link java.awt.image.BufferedImage BufferedImage}
     */
    public BufferedImage getCurrentFrameAsBufferedImage() {
        return frameCollection.get(currentFrameIndex).asBufferedImage();
    }

    /**
     * Resets the frame collection and re-loads it with the given number of frameCollection
     *
     * @param numberOfFrames
     *         How many frameCollection to use (must be at least 1)
     *
     * @see AnimationFrame
     */
    void setFrames(int numberOfFrames) {
        // clear frame collection if previously set
        frameCollection.clear();
        if (numberOfFrames == 1) {
            frameCollection.add(new AnimationFrame(this, 0));
            return;
        }
        for (int i = 0; i < numberOfFrames; i++) {
            frameCollection.add(new AnimationFrame(this, i));
        }
    }

}
