package net.egartley.beyondorigins.logic.events;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;

/**
 * A custom "event" that can be used for gathering information from a collision
 * that has occured
 * {@link net.egartley.beyondorigins.logic.collision.EntityEntityCollision
 * EntityEntityCollision}
 * 
 * @author Evan Gartley
 */
public class EntityEntityCollisionEvent {

	public static final byte TOP_SIDE = 0;
	public static final byte LEFT_SIDE = 1;
	public static final byte BOTTOM_SIDE = 2;
	public static final byte RIGHT_SIDE = 3;

	private int tolerance;

	/**
	 * The numerical representation for the side that the collision occured at
	 * 
	 * @see #TOP_SIDE
	 * @see #BOTTOM_SIDE
	 * @see #LEFT_SIDE
	 * @see #RIGHT_SIDE
	 */
	public byte collidedSide = -1;

	public EntityEntityCollision invoker;

	/**
	 * Creates a new entity-entity collision event, then calculates
	 * {@link #collidedSide}
	 * 
	 * @param invoker
	 *            The
	 *            {@link net.egartley.beyondorigins.logic.collision.EntityEntityCollision
	 *            EntityEntityCollision} that occured
	 */
	public EntityEntityCollisionEvent(EntityEntityCollision invoker) {
		this.invoker = invoker;
		// round player speed to int, ex. 1.6 would be 2
		tolerance = (int) (Entities.PLAYER.speed + 0.5);
		EntityBoundary collider = (EntityBoundary) invoker.boundary1;
		EntityBoundary into = (EntityBoundary) invoker.boundary2;
		if (into.right - tolerance <= collider.left && collider.left <= into.right) {
			collidedSide = RIGHT_SIDE;
		} else if (into.left <= collider.right && collider.right <= into.left + tolerance) {
			collidedSide = LEFT_SIDE;
		} else if (into.top <= collider.bottom && collider.bottom <= into.top + tolerance) {
			collidedSide = TOP_SIDE;
		} else if (into.bottom - tolerance <= collider.top && collider.top <= into.bottom) {
			collidedSide = BOTTOM_SIDE;
		} else {
			Debug.error("Could not calcuate a collided side! (between " + invoker.entities.get(0) + " and "
					+ invoker.entities.get(1) + ")");
		}
	}

}
