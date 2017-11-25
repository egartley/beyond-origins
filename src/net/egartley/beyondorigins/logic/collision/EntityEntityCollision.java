package net.egartley.beyondorigins.logic.collision;

import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Entity;

public class EntityEntityCollision extends Collision {

	public Entity entity1, entity2;
	
	public EntityEntityCollision(EntityBoundary b1, EntityBoundary b2) {
		super(b1, b2);
		entity1 = b1.entity;
		entity2 = b2.entity;
	}

	@Override
	public void onCollision() {
		System.out.println("Oof!");
	}

}
