package net.egartley.beyondorigins.logic.collision;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Entity;

/**
 * A collision between two different entities
 * 
 * @author Evan Gartley
 */
public class EntityEntityCollision {

	/**
	 * Whether or not the two boundaries are collided with one another
	 */
	public boolean isCollided;
	/**
	 * Forgot what this does...
	 */
	public boolean previouslyCollided;

	private boolean firedEvent;

	private EntityEntityCollisionEvent event;

	public EntityBoundary boundary1;
	public EntityBoundary boundary2;
	public Rectangle rectangle1;
	public Rectangle rectangle2;

	/**
	 * One of the two entities that are to collide
	 */
	public ArrayList<Entity> entities;

	/**
	 * Creates a new collision between two entity boundaries
	 * 
	 * @param boundary1
	 *            First entity's boundary
	 * @param boundary2
	 *            Second entity's boundary
	 */
	public EntityEntityCollision(EntityBoundary boundary1, EntityBoundary boundary2) {
		this.boundary1 = boundary1;
		this.boundary2 = boundary2;
		rectangle1 = this.boundary1.asRectangle();
		rectangle2 = this.boundary2.asRectangle();
		rectangle1.width++;
		rectangle2.width++;
		entities = new ArrayList<Entity>();
		entities.add(boundary1.parent);
		entities.add(boundary2.parent);
	}

	/**
	 * Checks to see if the two boundaries are collided with one another, and calls
	 * {@link #onCollide(EntityEntityCollisionEvent)} when collided, then
	 * {@link #onCollisionEnd(EntityEntityCollisionEvent)} after it has ended
	 */
	public void tick() {
		rectangle1.x = boundary1.x - 1;
		rectangle2.x = boundary2.x - 1;
		rectangle1.y = boundary1.y - 1;
		rectangle2.y = boundary2.y - 1;
		isCollided = rectangle1.intersects(rectangle2);

		if (isCollided == true && firedEvent == false) {
			event = new EntityEntityCollisionEvent(this);
			onCollide_internal();
			onCollide(event);
			firedEvent = true;
		}
		if (isCollided == false && firedEvent == true) {
			onCollisionEnd_internal();
			onCollisionEnd(event);
			firedEvent = false;
		}
		if (previouslyCollided != isCollided) {
			setBoundaryColors();
		}

		previouslyCollided = isCollided;
	}

	/**
	 * This is called <b>once</b> after the collision occurs
	 * 
	 * @param event
	 *            The collision's event
	 */
	public void onCollide(EntityEntityCollisionEvent event) {

	}

	/**
	 * This is called <b>once</b> after the collision ends
	 * 
	 * @param event
	 *            The collision's event
	 */
	public void onCollisionEnd(EntityEntityCollisionEvent event) {

	}

	/**
	 * Sets/updates the colors for both boundaries
	 */
	public void setBoundaryColors() {
		determineBoundaryColors(entities.get(0).boundaries);
		determineBoundaryColors(entities.get(1).boundaries);
	}

	private void determineBoundaryColors(ArrayList<EntityBoundary> e) {
		for (EntityBoundary boundary : e) {
			determineBoundaryColor(boundary);
		}
	}

	private void determineBoundaryColor(EntityBoundary e) {
		if (e.isCollided == false) {
			if (e.parent.isStatic == true) {
				e.drawColor = Color.BLACK;
			} else {
				e.drawColor = Color.YELLOW;
			}
		} else {
			if (e.parent.isStatic == true) {
				e.drawColor = Color.YELLOW;
			} else {
				e.drawColor = Color.RED;
			}
		}
	}

	protected void onCollide_internal() {
		entities.get(0).lastCollision = this;
		entities.get(1).lastCollision = this;
		entities.get(0).isCollided = true;
		entities.get(1).isCollided = true;
		boundary1.isCollided = true;
		boundary2.isCollided = true;
	}

	protected void onCollisionEnd_internal() {
		boundary1.isCollided = false;
		boundary2.isCollided = false;
		for (EntityBoundary boundary : entities.get(0).boundaries) {
			if (boundary.isCollided == true) {
				entities.get(0).isCollided = true;
				break;
			}
			entities.get(0).isCollided = false;
		}
		for (EntityBoundary boundary : entities.get(1).boundaries) {
			if (boundary.isCollided == true) {
				break;
			}
			entities.get(1).isCollided = false;
		}
	}

}
