package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Debug;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A row, or "strip", of individual frames for an entity (they are all the same height and width)
 *
 * @see SpriteSheet
 * @see Entity
 */
public class Sprite {

    /**
     * The image that contains all of the sprite's frames (its width must be a multiple of {@link #width})
     *
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
     * Returns the first frame ({@link #getFrame(int) getFrame(0)}) as a buffered image
     */
    BufferedImage toBufferedImage() {
        return toBufferedImage(0);
    }

    /**
     * Returns {@link #getFrame(int) getFrame(index)} as a buffered image
     */
    BufferedImage toBufferedImage(int index) {
        return Objects.requireNonNull(getFrame(index)).asBufferedImage();
    }

    /**
     * Sets {@link #frames}, clearing it if previously set
     *
     * @param number
     *         The amount of frames to set (must be at least 1)
     */
    void setFrames(int number) {
        frames.clear();
        if (number == 1) {
            // just one frame, so don't use a for loop
            frames.add(new AnimationFrame(this, 0));
            return;
        } else if (number <= 0) {
            Debug.error("When setting frames for a sprite, there needs to be at least one!");
            return;
        }
        for (int i = 0; i < number; i++)
            frames.add(new AnimationFrame(this, i));
    }

}
