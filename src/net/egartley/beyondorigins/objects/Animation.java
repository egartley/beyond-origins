package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Debug;

import java.awt.*;

/**
 * Represents a collection of {@link AnimationFrame AnimationFrame} objects,
 * which can be used for animating a {@link Sprite Sprite}
 *
 * @see AnimationFrame
 * @see Sprite
 * @see AnimatedEntity
 */
public class Animation {

    private byte delay = 0, threshold = 10;
    private int frameIndex, startIndex;
    /**
     * Set to true when the animation is no longer changing frames, false otherwise
     */
    public boolean isStopped;
    private boolean setStopFrame;

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
     * @param s {@link Sprite} to animate
     */
    public Animation(Sprite s) {
        sprite = s;
        frameIndex = 0;
        startIndex = 0;
        startFrame = sprite.frameCollection.get(startIndex);
        frame = startFrame;
    }

    private AnimationFrame nextFrame() {
        if (frameIndex + 1 == sprite.frameCollection.size()) {
            frameIndex = 0;
        } else {
            frameIndex++;
        }
        return sprite.frameCollection.get(frameIndex);
    }

    /**
     * <p>
     * Sets the animation's threshold, or interval, for when to go to the next frame
     * </p>
     * <p>
     * The tick method should be called roughly 60 times per second, therefore each
     * frame will be displayed for about <b>threshold � 60</b> seconds
     * </p>
     *
     * @param t New value for {@link #threshold}
     */
    public void setThreshold(int t) {
        if (t > 127 || t < -128) {
            Debug.warning("tried to set an animation threshold outside of the accepted range (-127 to 127)!");
            return;
        }
        threshold = (byte) t;
    }

    /**
     * Resume the animation. Does nothing if already running
     */
    public void resume() {
        isStopped = false;
    }

    /**
     * Pauses the animation. Does nothing if already paused
     */
    public void pause() {
        isStopped = true;
    }

    /**
     * Stops the animation, and resets the displayed frame to the starting frame.
     * Does nothing if already stopped
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
     * @param graphics {@link java.awt.Graphics Graphics}
     * @param x        The x-axis coordinate
     * @param y        The y-axis coordinate
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
