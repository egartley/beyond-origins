package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

/**
 * An {@link net.egartley.beyondorigins.objects.Entity Entity} with associated
 * animations
 * 
 * @see Entity
 * @see Animation
 */
public abstract class AnimatedEntity extends Entity {

	/**
	 * The current animation that is used while rendering and in tick()
	 */
	public Animation animation;
	/**
	 * All of the animations that <i>could</i> be used while rendering
	 * 
	 * @see Animation
	 */
	public ArrayList<Animation> animationCollection = new ArrayList<Animation>();

	/**
	 * Creates a new animated entity, while setting
	 * {@link net.egartley.beyondorigins.objects.Entity#isAnimated
	 * Entity.isAnimated} to true and
	 * {@link net.egartley.beyondorigins.objects.Entity#isStatic Entity.isStatic} to
	 * false
	 * 
	 * @param id
	 *            Human-readable ID for the entity
	 * @see Entity
	 */
	public AnimatedEntity(String id) {
		super(id);
		isAnimated = true;
		isStatic = false;
	}

	/**
	 * Sets {@link #animationCollection}
	 * 
	 * @see Animation
	 */
	public abstract void setAnimationCollection();

}
