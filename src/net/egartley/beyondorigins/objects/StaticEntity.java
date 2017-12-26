package net.egartley.beyondorigins.objects;

/**
 * An entity that cannot be animated
 * 
 * @author Evan Gartley
 * @see Entity
 */
public abstract class StaticEntity extends Entity {

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
