package net.egartley.beyondorigins.core.logic.events;

import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.entities.Entities;

public class EntityEntityCollisionEvent {

    public byte collidedSide = -1;
    public EntityEntityCollision invoker;
    public static final byte TOP_SIDE = 0;
    public static final byte LEFT_SIDE = 1;
    public static final byte RIGHT_SIDE = 3;
    public static final byte BOTTOM_SIDE = 2;

    public EntityEntityCollisionEvent(EntityEntityCollision invoker) {
        this.invoker = invoker;
        // calculate the side in which the collision occurred
        calculateCollidedSide((int) Entities.PLAYER.speed, invoker.boundaries[0], invoker.boundaries[1]);
    }

    private void calculateCollidedSide(int tolerance, EntityBoundary boundary1, EntityBoundary boundary2) {
        if (Calculate.isEntityWithinToleranceOf(boundary1, boundary2, Entity.DIRECTION_LEFT, tolerance)) {
            collidedSide = LEFT_SIDE;
        } else if (Calculate.isEntityWithinToleranceOf(boundary1, boundary2, Entity.DIRECTION_RIGHT, tolerance)) {
            collidedSide = RIGHT_SIDE;
        }
        if (Calculate.isEntityWithinToleranceOf(boundary1, boundary2, Entity.DIRECTION_UP, tolerance)) {
            collidedSide = TOP_SIDE;
        } else if (Calculate.isEntityWithinToleranceOf(boundary1, boundary2, Entity.DIRECTION_DOWN, tolerance)) {
            collidedSide = BOTTOM_SIDE;
        }
    }

}
