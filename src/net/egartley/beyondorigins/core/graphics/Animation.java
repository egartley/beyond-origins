package net.egartley.beyondorigins.core.graphics;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.core.abstracts.AnimatedEntity;
import net.egartley.beyondorigins.core.threads.AnimationClock;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

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
    private final int startIndex;
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
    private Image frame;
    /**
     * The frame that the animation will start at
     */
    private final Image startFrame;

    /**
     * Creates a new animation, with a default frame delay of 500 and start index of 0
     *
     * @param sprite Sprite to animate
     */
    public Animation(Sprite sprite) {
        this(sprite, 500, 0);
    }

    /**
     * Creates a new animation, with a default start index of 0
     *
     * @param sprite     Sprite to animate
     * @param frameDelay The delay, in milliseconds, between frames
     */
    public Animation(Sprite sprite, int frameDelay) {
        this(sprite, frameDelay, 0);
    }

    /**
     * Creates a new animation
     *
     * @param sprite     Sprite to animate
     * @param frameDelay The delay, in milliseconds, between frames
     * @param startIndex What frame index to begin at
     */
    public Animation(Sprite sprite, int frameDelay, int startIndex) {
        this.sprite = sprite;
        this.frameDelay = frameDelay;
        this.startIndex = startIndex;
        frameIndex = 0;
        startFrame = sprite.frames.get(startIndex);
        frame = startFrame;
        // this does NOT start the clock!
        clock = new AnimationClock(this);
    }

    /**
     * Returns the frame at {@link #frameIndex} within {@link #sprite}'s {@link Sprite#frames}
     */
    private Image getFrame() {
        return sprite.frames.get(frameIndex);
    }

    private Image nextFrame() {
        // check if we need to go back to the start of the animation or just go to the next one
        if (frameIndex + 1 >= sprite.frames.size())
            frameIndex = startIndex;
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

    /**
     * Starts the animation by starting {@link #clock}
     */
    public void start() {
        stopThread();
        clock = new AnimationClock(this);
        clock.start();
    }

    /**
     * Stops the animation by killing {@link #clock}, and then sets {@link #frame} to {@link #startFrame}
     */
    public void stop() {
        stop(true);
    }

    /**
     * Stops the animation by killing {@link #clock}
     *
     * @param resetFrame Whether or not to set {@link #frame} to {@link #startFrame}
     */
    public void stop(boolean resetFrame) {
        if (clock.isRunning) {
            stopThread();
            if (resetFrame) {
                frame = startFrame;
            }
        }
    }

    public boolean isRunning() {
        return clock.isRunning;
    }

    /**
     * Kills the clock's thread
     */
    private void stopThread() {
        if (clock != null) {
            clock.isRunning = false;
            try {
                clock.thread.join();
            } catch (InterruptedException e) {
                Debug.error(e);
            }
        }
    }

    /**
     * Renders {@link Animation#frame frame} at the specified position
     */
    public void render(Graphics graphics, int x, int y) {
        graphics.drawImage(frame, x, y);
    }

}
