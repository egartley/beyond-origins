package net.egartley.gamelib.objects;

import net.egartley.gamelib.graphics.Animation;

import java.awt.*;
import java.util.ArrayList;

/**
 * An {@link net.egartley.gamelib.objects.Entity Entity} with animations
 *
 * @see Animation
 */
public abstract class AnimatedEntity extends Entity {

    /**
     * The animation that is being used while rendering
     */
    protected Animation animation;
    /**
     * All of the animations that available to use
     *
     * @see Animation
     */
    protected ArrayList<Animation> animations = new ArrayList<>();

    /**
     * Creates a new animated entity, while setting {@link Entity#isAnimated} to <code>true</code> and {@link
     * Entity#isStatic} to <code>false</code>
     *
     * @see Entity#Entity(String) Entity(String)
     */
    public AnimatedEntity(String id) {
        super(id);
        isAnimated = true;
        isStatic = false;
    }

    /**
     * Sets the entity's animations
     *
     * @see #animations
     */
    public abstract void setAnimations();

    /**
     * Calls {@link Animation#render(Graphics, int, int)} for {@link #animation} and then {@link #drawDebug(Graphics)}
     */
    @Override
    public void render(Graphics graphics) {
        animation.render(graphics, (int) x, (int) y);
        drawDebug(graphics);
    }
}
