package net.egartley.beyondorigins.graphics;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.objects.AnimatedEntity;

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
     * Number of calls to {@link #tick()} until {@link #threshold} is reached
     */
    private byte delay = 0;
    /**
     * Whether or not the "stop" frame has been set (the frame displayed when the animation is stopped)
     */
    private boolean setStopFrame;
    /**
     * Number of calls to {@link #tick()} before switching to the next frame (which should be called 60 times per
     * second)
     *
     * @see #nextFrame()
     */
    private byte threshold = 10;
    /**
     * Set to true when the animation is no longer changing frames, false otherwise
     *
     * @see #stop()
     * @see #pause()
     */
    public boolean isStopped;
    /**
     * The {@link Sprite} to animate
     */
    public Sprite sprite;
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
        sprite = s;
        frameIndex = 0;
        startIndex = 0;
        startFrame = sprite.frames.get(startIndex);
        frame = startFrame;
    }

    /**
     * Creates a new animation
     *
     * @param s         Sprite to animate
     * @param threshold The animation's {@link #threshold}
     */
    public Animation(Sprite s, byte threshold) {
        this(s);
        this.threshold = threshold;
    }

    /**
     * Creates a new animation
     *
     * @param s          Sprite to animate
     * @param threshold  The animation's {@link #threshold}
     * @param startIndex The index to begin at within {@link Sprite#frames}
     */
    public Animation(Sprite s, byte threshold, int startIndex) {
        this(s, threshold);
        this.startIndex = startIndex;
        startFrame = sprite.frames.get(startIndex);
        frame = startFrame;
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

    public void setFrame(int index) {
        if (index >= sprite.frames.size()) {
            Debug.warning("Tried to set the frame of an animation to an index that is out-of-bounds");
            return;
        }
        frameIndex = index;
        frame = getFrame();
    }

    /**
     * Resume the animation. Does nothing if already running
     * <p>
     * Sets {@link #isStopped} to <code>false</code>
     */
    public void resume() {
        isStopped = false;
    }

    /**
     * Pauses the animation, by setting {@link #isStopped} to <code>true</code> (does nothing if already paused)
     */
    public void pause() {
        isStopped = true;
    }

    /**
     * Stops the animation, and resets the current frame to the starting frame (does nothing if already stopped)
     */
    public void stop() {
        pause();
        setStopFrame = false;
    }

    /**
     * Restarts the animation, by setting {@link #frameIndex} to {@link #startIndex}, {@link #delay} to <code>0</code>,
     * and {@link #isStopped} to <code>false</code>
     */
    public void restart() {
        frameIndex = startIndex;
        delay = 0;
        isStopped = false;
    }

    /**
     * Renders {@link Animation#frame frame} with {@link Graphics#drawImage(Image, int, int, ImageObserver)}
     */
    public void render(Graphics graphics, int x, int y) {
        graphics.drawImage(frame, x, y, null);
    }

    /**
     * Progresses the animation in accordance with {@link #threshold}, assuming 60 calls per second
     */
    public void tick() {
        if (!isStopped) {
            // animation isn't stopped, so progress it
            if (delay < threshold) {
                // we haven't reached the threshold yet, so increment delay until it's reached
                delay++;
            } else {
                // reached the threshold, reset delay
                delay = 0;
                // progress to next frame
                frame = nextFrame();
            }
        } else if (!setStopFrame) {
            // set current frame to the "start" frame
            frame = startFrame;
            // prevent from being set again until the animation finishes again
            setStopFrame = true;
        }
    }

}
