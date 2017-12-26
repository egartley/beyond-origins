package net.egartley.beyondorigins.logic.collision;

import java.awt.Rectangle;

import net.egartley.beyondorigins.logic.events.CollisionEvent;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.Boundary;

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
	 * Forgot what this does...
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
	 * @param boundary1
	 *            One of the two boundaries
	 * @param boundary2
	 *            The other of the two boundaries
	 * @see {@link net.egartley.beyondorigins.logic.interaction.Boundary Boundary}
	 */
	public Collision(Boundary boundary1, Boundary boundary2) {
		this.boundary1 = boundary1;
		this.boundary2 = boundary2;
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

		if (isCollided == true && firedEvent == false) {
			onCollide(new EntityEntityCollisionEvent(this));
			firedEvent = true;
		}
		if (isCollided == false && firedEvent == true) {
			onCollisionEnd(new EntityEntityCollisionEvent(this));
			firedEvent = false;
		}
		if (previouslyCollided != isCollided) {
			setBoundaryColors();
		}

		previouslyCollided = isCollided;
	}

	/**
	 * This method should set/update the colors for both boundaries
	 */
	public abstract void setBoundaryColors();

	/**
	 * This method is called <b>once</b> after the collision occurs
	 */
	public abstract void onCollide(CollisionEvent event);

	/**
	 * This method is called <b>once</b> after the collision ends
	 */
	public abstract void onCollisionEnd(CollisionEvent event);

}
