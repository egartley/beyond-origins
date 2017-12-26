package net.egartley.beyondorigins.logic.events;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.logic.collision.Collision;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;

/**
 * An extension of {@link CollisionEvent} meant for use with an
 * {@link net.egartley.beyondorigins.logic.collision.EntityEntityCollision
 * EntityEntityCollision}
 * 
 * @author Evan Gartley
 * @see CollisionEvent
 * @see {@link net.egartley.beyondorigins.logic.collision.EntityEntityCollision
 *      EntityEntityCollision}
 *
 */
public class EntityEntityCollisionEvent extends CollisionEvent {

	public static final byte		TOP			= 0, LEFT = 1, BOTTOM = 2, RIGHT = 3;
	public byte						collidedSide;
	private final byte				TOLERANCE	= 2;
	private EntityEntityCollision	parent;

	public EntityEntityCollisionEvent(Collision invoker) {
		this.invoker = invoker;
		parent = null;
		try {
			parent = (EntityEntityCollision) invoker;
		}
		catch (Exception e) {
			Debug.error("There was an error while attempting to cast the collision event's invoker to an EntityEntityCollision");
			e.printStackTrace();
		}
		if (parent != null) {
			EntityBoundary collider = parent.firstEntity.boundary, into = parent.secondEntity.boundary;
			if (into.right - TOLERANCE <= collider.left && collider.left <= into.right) {
				collidedSide = RIGHT;
			}
			else if (into.left <= collider.right && collider.right <= into.left + TOLERANCE) {
				collidedSide = LEFT;
			}
			else if (into.top <= collider.bottom && collider.bottom <= into.top + TOLERANCE) {
				collidedSide = TOP;
			}
			else if (into.bottom - TOLERANCE <= collider.top && collider.top <= into.bottom) {
				collidedSide = BOTTOM;
			}
		}
	}

}
