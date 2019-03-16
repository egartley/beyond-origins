package net.egartley.beyondorigins.graphics;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.threads.AnimationClock;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * Represents a collection of images, used for animating sprites
 *
 * @see Sprite
 * @see AnimatedEntity
 */
public class Animation {

    /**
     * Index in {@link Sprite#frames} for the frame to use when rendering (the current frame)
     */
    private int frameIndex;
    private int startIndex;
    /**
     * Milliseconds between frames
     */
    public int frameDelay;
    /**
     * The {@link Sprite} to animate
     */
    public Sprite sprite;
    /**
     * The "clock" that controls what frame is being displayed
     */
    public AnimationClock clock;
    /**
     * The frame that is currently being used when rendering
     */
    private BufferedImage frame;
    /**
     * The frame that the animation will start at
     */
    private BufferedImage startFrame;

    /**
     * Creates a new animation
     *
     * @param s Sprite to animate
     */
    public Animation(Sprite s) {
        this(s, 500);
    }

    /**
     * Creates a new animation
     *
     * @param s         Sprite to animate
     * @param frameDelay The delay, in milliseconds, between frames
     */
    public Animation(Sprite s, int frameDelay) {
        sprite = s;
        frameIndex = 0;
        startIndex = 0;
        startFrame = sprite.frames.get(startIndex);
        frame = startFrame;
        this.frameDelay = frameDelay;

        clock = new AnimationClock(this);
    }

    /**
     * Returns the frame at {@link #frameIndex} within {@link #sprite}'s {@link Sprite#frames}
     */
    private BufferedImage getFrame() {
        return sprite.frames.get(frameIndex);
    }

    private BufferedImage nextFrame() {
        // check if we need to go back to the start of the animation or just go to the next one
        if (frameIndex + 1 >= sprite.frames.size())
            frameIndex = 0;
        else
            frameIndex++;

        return getFrame();
    }

    public synchronized void setFrame(int index) {
        if (index >= sprite.frames.size()) {
            Debug.warning("Tried to set the frame of an animation to an index that is out-of-bounds");
            return;
        }
        frameIndex = index;
        frame = getFrame();
    }

    public void increment() {
        frame = nextFrame();
    }

    public void start() {
        stopThread();
        clock = new AnimationClock(this);
        clock.start();
    }

    public void stop() {
        stopThread();
        frame = startFrame;
    }

    private void stopThread() {
        if (clock != null) {
            // kill it
            clock.isRunning = false;
        }
    }

    /**
     * Renders {@link Animation#frame frame} with {@link Graphics#drawImage(Image, int, int, ImageObserver)}
     */
    public void render(Graphics graphics, int x, int y) {
        graphics.drawImage(frame, x, y, null);
    }

}
