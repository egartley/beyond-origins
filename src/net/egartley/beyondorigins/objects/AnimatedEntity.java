package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

/**
 * An {@link Entity} with associated animations
 * 
 * @author Evan Gartley
 * @see Entity
 * @see Animation
 */
public abstract class AnimatedEntity extends Entity {

	/**
	 * The current {@link Animation} to use with
	 * {@link Entity#render(java.awt.Graphics) render} and {@link Entity#tick()
	 * tick}
	 */
	public Animation			animation;
	/**
	 * All of the animations that could be used while rendering
	 * 
	 * @see Animation
	 */
	public ArrayList<Animation>	animationCollection	= new ArrayList<Animation>();

	/**
	 * Creates a new animated entity
	 * 
	 * @see Entity
	 */
	public AnimatedEntity() {
		isAnimated = true;
		isStatic = false;
	}

	/**
	 * Builds {@link #animationCollection}
	 * 
	 * @see #animationCollection
	 * @see Animation
	 */
	public abstract void setAnimationCollection();

}
