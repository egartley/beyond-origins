package net.egartley.gamelib.graphics;

import net.egartley.beyondorigins.Debug;
import net.egartley.gamelib.abstracts.AnimatedEntity;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.abstracts.StaticEntity;
import org.newdawn.slick.Image;

import java.util.ArrayList;

/**
 * A row, or "strip", of individual frames for an entity (they are all the same height and width)
 *
 * @see SpriteSheet
 * @see Entity
 */
public class Sprite {

    /**
     * All of the possible frames that the sprite could use
     *
     * @see AnimatedEntity
     * @see StaticEntity
     */
    ArrayList<Image> frames = new ArrayList<>();
    /**
     * Width in pixels
     */
    public int width;
    /**
     * Height in pixels
     */
    public int height;

    public Sprite(Image image) {
        this(image, 1);
    }

    public Sprite(Image row, int frames) {
        this(row, row.getWidth() / frames, row.getHeight(), frames);
    }

    public Sprite(Image row, int width, int height, int frames) {
        this.width = width;
        this.height = height;
        if (frames == 1) {
            this.frames.add(row.getSubImage(0, 0, width, height));
            return;
        } else if (frames <= 0) {
            Debug.error("When setting frames for a sprite, there needs to be at least one!");
            return;
        }
        for (int i = 0; i < frames; i++) {
            this.frames.add(row.getSubImage(i * width, 0, width, height));
        }
    }

    public Image asImage() {
        return frames.get(0);
    }

}
