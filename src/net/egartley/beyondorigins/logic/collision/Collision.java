package net.egartley.beyondorigins.logic.collision;

import java.awt.Color;
import java.awt.Rectangle;

import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.Boundary;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Entity;

/**
 * A collision between two different boundaries
 * 
 * @author Evan Gartley
 * @see {@link net.egartley.beyondorigins.logic.interaction.Boundary Boundary}
 * @see {@link net.egartley.beyondorigins.logic.interaction.EntityBoundary
 *      EntityBoundary}
 * @see {@link EntityEntityCollison}
 */
public abstract class Collision {

	/**
	 * Whether or not the two boundaries are collided with one another
	 */
	public boolean		isCollided;
	/**
	 * I kind of forgot what this signifies... carry on
	 */
	public boolean		previouslyCollided;

	private boolean		firedEvent;

	public Boundary		boundary1;
	public Boundary		boundary2;
	public Rectangle	rectangle1;
	public Rectangle	rectangle2;

	/**
	 * Creates a new collision between the two specified boundaries
	 * 
	 * @param b1
	 *            One of the two boundaries
	 * @param b2
	 *            The other of the two boundaries
	 * @see {@link net.egartley.beyondorigins.logic.interaction.Boundary Boundary}
	 */
	public Collision(Boundary b1, Boundary b2) {
		boundary1 = b1;
		boundary2 = b2;
		rectangle1 = boundary1.asRectangle();
		rectangle2 = boundary2.asRectangle();
	}

	/**
	 * Checks to see if the two boundaries are collided with one another
	 */
	public void tick()
	{
		rectangle1.x = boundary1.x;
		rectangle2.x = boundary2.x;
		rectangle1.y = boundary1.y;
		rectangle2.y = boundary2.y;
		isCollided = rectangle1.intersects(rectangle2);

		if (isCollided && !firedEvent) {
			onCollision(new EntityEntityCollisionEvent(this));
			firedEvent = true;
		}
		if (!isCollided && firedEvent) {
			collisionEnd(new EntityEntityCollisionEvent(this));
			firedEvent = false;
		}
		if (previouslyCollided != isCollided) {
			setBoundaryColors();
		}

		previouslyCollided = isCollided;
	}

	private void setBoundaryColors()
	{
		Entity e1 = ((EntityBoundary) boundary1).entity;
		Entity e2 = ((EntityBoundary) boundary2).entity;
		Color c1 = Color.BLACK, c2 = Color.YELLOW;
		if (isCollided) {
			c1 = Color.YELLOW;
			c2 = Color.RED;
		}

		if (!e1.isCollided) {
			if (e1.isStatic) {
				e1.boundary.drawColor = c1;
			}
			else {
				e1.boundary.drawColor = c2;
			}
		}
		if (!e2.isCollided) {
			if (e2.isStatic) {
				e2.boundary.drawColor = c1;
			}
			else {
				e2.boundary.drawColor = c2;
			}
		}
	}

	/**
	 * This method is called <i>once</i> after the collision occurs
	 */
	public abstract void onCollision(EntityEntityCollisionEvent event);

	/**
	 * This method is called <i>once</i> after the collision ends
	 */
	public abstract void collisionEnd(EntityEntityCollisionEvent event);

}
