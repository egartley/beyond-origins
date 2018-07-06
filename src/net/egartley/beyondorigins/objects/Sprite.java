package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

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
     * @see #frames
     */
    BufferedImage strip;
    /**
     * All of the possible frames that the sprite could use, which are derived from {@link #strip}
     *
     * @see AnimatedEntity
     * @see StaticEntity
     */
    ArrayList<AnimationFrame> frames = new ArrayList<>();
    /**
     * The width in pixels for each frame
     */
    public int width;
    /**
     * The height in pixels for each frame
     */
    public int height;

    public Sprite(BufferedImage image, int width, int height) {
        strip = image;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the frame at the given index (from {@link #frames})
     *
     * @param index
     *         Index of the frame to return
     */
    private AnimationFrame getFrame(int index) {
        if (index >= frames.size()) {
            return null;
        }
        return frames.get(index);
    }

    /**
     * Returns the current frame as a buffered image
     */
    public BufferedImage asBufferedImage(int index) {
        return Objects.requireNonNull(getFrame(index)).asBufferedImage();
    }

    /**
     * Resets {@link #frames} and re-loads it with the given number of frames
     *
     * @param quantity
     *         How many frames to use (must be at least 1)
     */
    void setFrames(int quantity) {
        frames.clear();
        if (quantity == 1) {
            frames.add(new AnimationFrame(this, 0));
            return;
        }
        for (int i = 0; i < quantity; i++) {
            frames.add(new AnimationFrame(this, i));
        }
    }

}
