package net.egartley.beyondorigins.logic.collision;

import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
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
	public void onCollision(EntityEntityCollisionEvent event)
	{
	}

	@Override
	public void collisionEnd(EntityEntityCollisionEvent event)
	{
	}

}
