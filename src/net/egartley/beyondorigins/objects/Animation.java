package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Debug;

import java.awt.*;

/**
 * Represents a collection of {@link AnimationFrame AnimationFrame} objects, which can be used for animating a {@link
 * Sprite Sprite}
 *
 * @see AnimationFrame
 * @see Sprite
 * @see AnimatedEntity
 */
public class Animation {

    private int frameIndex;
    private int startIndex;
    private byte delay = 0;
    private byte threshold = 10;
    private boolean setStopFrame;

    /**
     * Set to true when the animation is no longer changing frames, false otherwise
     */
    public boolean isStopped;

    /**
     * The {@link Sprite} to animate
     */
    public Sprite sprite;
    /**
     * The frame that is currently being used while rendering
     */
    private AnimationFrame frame;
    /**
     * The frame that the animation will start at
     */
    private AnimationFrame startFrame;

    /**
     * Creates a new animation
     *
     * @param s
     *         Sprite to animate
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
     * @param s
     *         Sprite to animate
     * @param threshold
     *         The animation's threshold, see {@link #setThreshold(int)}
     */
    public Animation(Sprite s, byte threshold) {
        this(s);
        this.threshold = threshold;
    }

    /**
     * Creates a new animation
     *
     * @param s
     *         Sprite to animate
     * @param threshold
     *         The animation's threshold, see {@link #setThreshold(int)}
     * @param startIndex
     *         The frame index to start the animation at
     */
    public Animation(Sprite s, byte threshold, int startIndex) {
        this(s, threshold);
        this.startIndex = startIndex;
        startFrame = sprite.frames.get(startIndex);
    }

    private AnimationFrame getFrame() {
        return sprite.frames.get(frameIndex);
    }

    private AnimationFrame nextFrame() {
        if (frameIndex + 1 == sprite.frames.size()) {
            frameIndex = 0;
        } else {
            frameIndex++;
        }
        return getFrame();
    }

    /**
     * <p>
     * Sets the animation's threshold, or interval, for when to go to the next frame
     * </p>
     * <p>
     * The tick method should be called roughly 60 times per second, therefore each frame will be displayed for about
     * <b>threshold / 60</b> seconds
     * </p>
     * <p>In other words, the higher the threshold, the longer each frame will show for</p>
     *
     * @param t
     *         New value for {@link #threshold}
     */
    public void setThreshold(int t) {
        if (t > 127 || t < -127) {
            Debug.warning("tried to set an animation threshold outside of the accepted range [-127 to 127]!");
            return;
        }
        threshold = (byte) t;
    }

    public void setFrame(int index) {
        if (index >= sprite.frames.size()) {
            Debug.warning("Tried to set the frame for an animation to an invalid index");
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
     * Pauses the animation. Does nothing if already paused
     * <p>
     * Sets {@link #isStopped} to <code>true</code>
     */
    public void pause() {
        isStopped = true;
    }

    /**
     * Stops the animation, and resets the displayed frame to the starting frame, but does nothing if already stopped
     */
    public void stop() {
        isStopped = true;
        setStopFrame = false;
    }

    /**
     * Restarts the animation. If already running, the animation will start over
     */
    public void restart() {
        frameIndex = startIndex;
        delay = 0;
        isStopped = false;
    }

    /**
     * Renders {@link Animation#frame frame}
     *
     * @param graphics
     *         {@link java.awt.Graphics Graphics}
     * @param x
     *         The x-axis coordinate
     * @param y
     *         The y-axis coordinate
     *
     * @see AnimationFrame
     */
    public void render(Graphics graphics, int x, int y) {
        graphics.drawImage(frame.asBufferedImage(), x, y, null);
    }

    /**
     * Should be called 60 times per second, within a tick thread
     */
    public void tick() {
        if (!isStopped) {
            if (delay < threshold) {
                delay++;
            } else {
                delay = 0;
                frame = nextFrame();
            }
        } else {
            if (!setStopFrame) {
                frame = startFrame;
                setStopFrame = true;
            }
        }
    }

}
