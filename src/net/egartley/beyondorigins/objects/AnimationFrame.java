package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Debug;

import java.awt.image.BufferedImage;

/**
 * A single frame within an {@link Animation}, which is basically just a buffered image
 */
public class AnimationFrame {

    private Sprite parent;
    private BufferedImage bufferedImage;

    /**
     * Creates a frame for use in an {@link Animation}
     *
     * @param parent
     *         The sprite in which to base the frame on
     * @param index
     *         The index of the frame within {@link Sprite#strip}
     */
    AnimationFrame(Sprite parent, int index) {
        this.parent = parent;
        try {
            bufferedImage = parent.strip.getSubimage(index * parent.width, 0, parent.width, parent.height);
        } catch (Exception e) {
            Debug.error("There was an error while trying to create an animation frame (are the coordinates and " +
                    "dimensions within bounds?)");
            e.printStackTrace();
        }
    }

    /**
     * Returns {@link #parent}
     */
    public Sprite getParent() {
        return parent;
    }

    /**
     * Returns {@link #bufferedImage}
     */
    BufferedImage asBufferedImage() {
        return bufferedImage;
    }

}
