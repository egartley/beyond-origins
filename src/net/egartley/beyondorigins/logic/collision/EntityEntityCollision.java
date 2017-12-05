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

	public Entity firstEntity, secondEntity;

	/**
	 * Creates a new collision between two entities
	 * 
	 * @param first
	 *            EntityBoundary of the first entity
	 * @param second
	 *            EntityBoundary of the second entity
	 * 
	 * @see Collision
	 * @see Entity
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
	public void afterCollision(EntityEntityCollisionEvent event)
	{
	}

}
