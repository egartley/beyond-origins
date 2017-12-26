package net.egartley.beyondorigins.logic.collision;

import java.awt.Color;

import net.egartley.beyondorigins.logic.events.CollisionEvent;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Entity;

/**
 * A collision between two different entities
 * 
 * @author Evan Gartley
 * @see Collision
 * @see Entity
 */
public class EntityEntityCollision extends Collision {

	/**
	 * One of the two entities that are to collide
	 */
	public Entity	firstEntity;
	/**
	 * The other of the two entities that are to collide
	 */
	public Entity	secondEntity;

	/**
	 * Creates a new collision between two entities
	 * 
	 * @param first
	 *            {@link net.egartley.beyondorigins.logic.interaction.EntityBoundary
	 *            EntityBoundary} of the first entity
	 * @param second
	 *            {@link net.egartley.beyondorigins.logic.interaction.EntityBoundary
	 *            EntityBoundary} of the second entity
	 * 
	 * @see {@link net.egartley.beyondorigins.logic.collision.Collision Collision}
	 * @see {@link net.egartley.beyondorigins.objects.Entity Entity}
	 */
	public EntityEntityCollision(EntityBoundary first, EntityBoundary second) {
		super(first, second);
		firstEntity = first.entity;
		secondEntity = second.entity;
	}

	@Override
	public void onCollide(CollisionEvent event)
	{
	}

	@Override
	public void onCollisionEnd(CollisionEvent event)
	{
	}

	@Override
	public void setBoundaryColors()
	{
		calculateBoundaryColor(firstEntity);
		calculateBoundaryColor(secondEntity);
	}

	private void calculateBoundaryColor(Entity e)
	{
		if (e.isCollided == false) {
			if (e.isStatic == true) {
				e.boundary.drawColor = Color.BLACK;
			}
			else {
				e.boundary.drawColor = Color.YELLOW;
			}
		}
		else {
			if (e.isStatic == true) {
				e.boundary.drawColor = Color.YELLOW;
			}
			else {
				e.boundary.drawColor = Color.RED;
			}
		}
	}

}
