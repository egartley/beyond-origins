package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;

/**
 * An {@link Entity} with associated animations
 * 
 * @author Evan Gartley
 * @see Entity
 * @see Animation
 */
public abstract class AnimatedEntity extends Entity {

	/**
	 * The current animation that is used while rendering and in tick()
	 */
	public Animation					animation;
	/**
	 * All of the animations that <i>could</i> be used while rendering
	 * 
	 * @see Animation
	 */
	public ArrayList<Animation>			animationCollection	= new ArrayList<Animation>();
	/**
	 * The most recent collision that has occured for this entity. If no collisions
	 * have occured within this entity's lifetime, this will be null
	 */
	public EntityEntityCollision		lastCollision;
	/**
	 * The most recent collision event to have occured. This will be null if no
	 * collision event has yet to take place
	 */
	public EntityEntityCollisionEvent	lastCollisionEvent;

	/**
	 * Creates a new animated entity, while setting {@link Entity#isAnimated} to
	 * true and {@link Entity#isStatic} to false
	 * 
	 * @see Entity
	 */
	public AnimatedEntity(String id) {
		super(id);
		isAnimated = true;
		isStatic = false;
	}

	/**
	 * This method should be used for building {@link #animationCollection}
	 * 
	 * @see Animation
	 */
	public abstract void setAnimationCollection();

}
