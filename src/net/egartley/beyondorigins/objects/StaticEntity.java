package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;

/**
 * An entity that cannot be animated
 * 
 * @author Evan Gartley
 * @see Entity
 */
public abstract class StaticEntity extends Entity {

	/**
	 * The entity's frame that is used while rendering
	 */
	public BufferedImage frame;

	/**
	 * Creates a new static entity, while setting {@link Entity#isAnimated} to false
	 * and {@link Entity#isStatic} to true
	 * 
	 * @see Entity
	 */
	public StaticEntity(String id) {
		super(id);
		isAnimated = false;
		isStatic = true;
	}

}
