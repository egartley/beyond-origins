package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Debug;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A row, or "strip", of individual frames for an entity (they are all the same height and width)
 *
 * @see SpriteSheet
 * @see Entity
 */
public class Sprite {

    /**
     * All of the possible frames that the sprite could use, which are derived from {@link #strip}
     *
     * @see AnimatedEntity
     * @see StaticEntity
     */
    ArrayList<BufferedImage> frames = new ArrayList<>();
    /**
     * Width in pixels
     */
    public int width;
    /**
     * Height in pixels
     */
    public int height;

    public Sprite(BufferedImage row, int width, int height, int frames) {
        this.width = width;
        this.height = height;

        this.frames.clear();
        if (frames == 1) {
            this.frames.add(row.getSubimage(0, 0, width, height));
            return;
        } else if (frames <= 0) {
            Debug.error("When setting frames for a sprite, there needs to be at least one!");
            return;
        }
        for (int i = 0; i < frames; i++) {
            this.frames.add(row.getSubimage(i * width, 0, width, height));
        }
    }

    /**
     * Returns the first frame in {@link #frames} as a buffered image
     */
    BufferedImage toBufferedImage() {
        return toBufferedImage(0);
    }

    /**
     * Returns the frame at the index as a buffered image
     */
    BufferedImage toBufferedImage(int index) {
        if (index >= frames.size()) {
            return null;
        }
        return frames.get(index);
    }

}
