package net.egartley.beyondorigins.logic.events;

import net.egartley.beyondorigins.logic.collision.Collision;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;

public class EntityEntityCollisionEvent {

	public static final byte	TOP	= 0, LEFT = 1, BOTTOM = 2, RIGHT = 3;
	public byte					collidedSide;
	public Collision			invoker;

	public EntityEntityCollisionEvent(Collision invoker) {
		this.invoker = invoker;
		EntityEntityCollision c = null;
		try {
			c = (EntityEntityCollision) this.invoker;
		}
		catch (Exception e) {
			System.out.println("There was an error while attempting to cast the collision event's invoker to an EntityEntityCollision");
			e.printStackTrace();
		}
		if (c != null) {
			EntityBoundary collider = c.firstEntity.boundary, into = c.secondEntity.boundary;
			if (into.right - 2 <= collider.left && collider.left <= into.right) {
				collidedSide = RIGHT;
			}
			else if (into.left <= collider.right && collider.right <= into.left + 2) {
				collidedSide = LEFT;
			}
			else if (into.top <= collider.bottom && collider.bottom <= into.top + 2) {
				collidedSide = TOP;
			}
			else if (into.bottom - 2 <= collider.top && collider.top <= into.bottom) {
				collidedSide = BOTTOM;
			}
		}
	}

}
