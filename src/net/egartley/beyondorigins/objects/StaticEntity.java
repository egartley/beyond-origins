package net.egartley.beyondorigins.objects;

/**
 * An entity that cannot be animated
 * 
 * @author Evan Gartley
 * @see Entity
 */
public abstract class StaticEntity extends Entity {

	/**
	 * Creates a new static entity
	 * 
	 * @see Entity
	 */
	public StaticEntity() {
		isAnimated = false;
		isStatic = true;
	}

}
