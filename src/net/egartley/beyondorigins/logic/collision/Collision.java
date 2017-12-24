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
	 * Forgot what this does...
	 */
	public boolean		previouslyCollided;

	private boolean		firedEvent;

	private Color		collidedStaticColor	= Color.YELLOW, collidedAnimatedColor = Color.RED, defaultStaticColor = Color.BLACK,
			defaultAnimatedColor = Color.YELLOW;

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
			onCollision(new EntityEntityCollisionEvent(this));
			firedEvent = true;
		}
		if (isCollided == false && firedEvent == true) {
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
		Entity entity1 = ((EntityBoundary) boundary1).entity;
		Entity entity2 = ((EntityBoundary) boundary2).entity;
		Color staticColor = defaultStaticColor, animatedColor = defaultAnimatedColor;
		if (isCollided) {
			staticColor = collidedStaticColor;
			animatedColor = collidedAnimatedColor;
		}

		if (entity1.isCollided == false) {
			if (entity1.isStatic == true) {
				entity1.boundary.drawColor = staticColor;
			}
			else {
				entity1.boundary.drawColor = animatedColor;
			}
		}
		if (entity2.isCollided == false) {
			if (entity2.isStatic == true) {
				entity2.boundary.drawColor = staticColor;
			}
			else {
				entity2.boundary.drawColor = animatedColor;
			}
		}
	}

	/**
	 * This method is called <b>once</b> after the collision occurs
	 */
	public abstract void onCollision(EntityEntityCollisionEvent event);

	/**
	 * This method is called <b>once</b> after the collision ends
	 */
	public abstract void collisionEnd(EntityEntityCollisionEvent event);

}
