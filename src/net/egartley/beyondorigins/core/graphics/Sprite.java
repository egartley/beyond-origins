package net.egartley.beyondorigins.core.graphics;

import net.egartley.beyondorigins.Debug;
import org.newdawn.slick.Image;

import java.util.ArrayList;

/**
 * A row, or "strip", of individual frames for an entity (they are all the same height and width)
 */
public class Sprite {

    public int width;
    public int height;
    public ArrayList<Image> frames = new ArrayList<>();

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
        return asImage(0);
    }

    public Image asImage(int frameIndex) {
        return frames.get(frameIndex);
    }

}
