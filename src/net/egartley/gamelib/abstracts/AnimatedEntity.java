package net.egartley.gamelib.abstracts;

import net.egartley.gamelib.graphics.Animation;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.graphics.SpriteSheet;

import java.awt.*;
import java.util.ArrayList;

/**
 * An {@link Entity Entity} with animations
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
     * Creates a new animated entity, while setting {@link Entity#isAnimated} to <code>true</code>
     *
     * @see Entity#Entity(String, Sprite) Entity(String, Sprite)
     */
    public AnimatedEntity(String id, Sprite sprite) {
        super(id, sprite);
        isAnimated = true;
        setAnimations();
    }

    public AnimatedEntity(String id, SpriteSheet sheet) {
        super(id, sheet.sprites.get(0));
        isAnimated = true;
        sprites = sheet.sprites;
        setAnimations();
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
        animation.render(graphics, x(), y());
    }
}
