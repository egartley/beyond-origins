package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

/**
 * An {@link net.egartley.beyondorigins.objects.Entity Entity} with associated animations
 *
 * @see Entity
 * @see Animation
 */
public abstract class AnimatedEntity extends Entity {

    /**
     * The current animation that is used while rendering and in tick()
     */
    protected Animation animation;
    /**
     * All of the animations that <i>could</i> be used while rendering
     *
     * @see Animation
     */
    protected ArrayList<Animation> animations = new ArrayList<>();

    /**
     * Creates a new animated entity, while setting {@link net.egartley.beyondorigins.objects.Entity#isAnimated
     * Entity.isAnimated} to <code>true</code> and {@link net.egartley.beyondorigins.objects.Entity#isStatic
     * Entity.isStatic} to <code>false</code>
     *
     * @param id
     *         Human-readable ID for the entity
     */
    public AnimatedEntity(String id) {
        super(id);
        isAnimated = true;
        isStatic = false;
    }

    /**
     * Sets {@link #animations}
     *
     * @see Animation
     */
    public abstract void setAnimationCollection();

    @Override
    public void tick() {
        super.tick();

        animations.forEach(Animation::tick);
    }
}
