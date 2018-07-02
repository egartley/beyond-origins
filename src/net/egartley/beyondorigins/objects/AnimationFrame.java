package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;

/**
 * A single frame within an {@link Animation}
 *
 * @see Animation
 */
public class AnimationFrame {

    private Sprite parent;
    private BufferedImage bufferedImage;

    /**
     * Creates a new frame for use in an {@link Animation}
     *
     * @param parent The {@link net.egartley.beyondorigins.objects.Sprite Sprite} in
     *               which to base the frame around
     * @param index  The index of the frame within its parent's {@link Sprite#strip}
     */
    AnimationFrame(Sprite parent, int index) {
        this.parent = parent;
        try {
            bufferedImage = parent.strip.getSubimage(index * parent.frameWidth, 0, parent.frameWidth, parent.frameHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the frame's "parent" sprite ({@link #parent})
     *
     * @return The sprite in which the frame is based on
     */
    public Sprite getParent() {
        return parent;
    }

    /**
     * @return The frame as {@link BufferedImage}
     */
    BufferedImage asBufferedImage() {
        return bufferedImage;
    }

}
