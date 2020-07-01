package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.graphics.Animation;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

/**
 * An {@link Entity Entity} with animations
 *
 * @see Animation
 */
public abstract class AnimatedEntity extends Entity {

    /**
     * The animation that is currently being used
     */
    protected Animation animation;
    /**
     * All of the animations that available to use
     */
    protected ArrayList<Animation> animations = new ArrayList<>();

    public AnimatedEntity(String id) {
        super(id, (Sprite) null);
        isAnimated = true;
        setAnimations();
    }

    public AnimatedEntity(String id, SpriteSheet sheet) {
        super(id, sheet);
        isAnimated = true;
        setAnimations();
    }

    /**
     * Sets the entity's animations
     *
     * @see #animations
     */
    public abstract void setAnimations();

    /**
     * Changes {@link #animation}
     *
     * @param i The index of the animation to switch to in {@link #animations}
     */
    protected void switchAnimation(int i) {
        if (i >= animations.size()) {
            Debug.warning("Tried to switch to an animation at an invalid index");
            return;
        }
        if (animations.indexOf(animation) != i) {
            // this prevents the same animation being set again
            animation.stop(false);
            animation = animations.get(i);
        }
    }

    /**
     * Calls {@link Animation#render(Graphics, int, int)} for {@link #animation} and then {@link #drawDebug(Graphics)}
     */
    @Override
    public void render(Graphics graphics) {
        animation.render(graphics, x(), y());
        if (Game.debug) {
            drawDebug(graphics);
        }
    }
}
