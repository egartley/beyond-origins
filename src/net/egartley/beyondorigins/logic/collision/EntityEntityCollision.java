package net.egartley.beyondorigins.logic.collision;

import net.egartley.beyondorigins.logic.interaction.Boundary;
import net.egartley.beyondorigins.objects.Entity;

public class EntityEntityCollision extends Collision {

	public Entity entity1, entity2;
	
	public EntityEntityCollision(Boundary b1, Boundary b2) {
		super(b1, b2);
		
	}

}
