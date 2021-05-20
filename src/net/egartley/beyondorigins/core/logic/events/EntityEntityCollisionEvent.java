package net.egartley.beyondorigins.core.logic.events;

import net.egartley.beyondorigins.core.enums.Direction;
import net.egartley.beyondorigins.core.enums.Side;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.entities.Entities;

/**
 * An event that occurs when two different entities collide with one another
 */
public class EntityEntityCollisionEvent {

    public Side collidedSide;
    public EntityEntityCollision invoker;

    public EntityEntityCollisionEvent(EntityEntityCollision invoker) {
        this.invoker = invoker;
        calculateCollidedSide((int) Entities.PLAYER.speed, invoker.boundaries[0], invoker.boundaries[1]);
    }

    private void calculateCollidedSide(int tolerance, EntityBoundary boundary1, EntityBoundary boundary2) {
        if (Calculate.isEntityWithinToleranceOf(boundary1, boundary2, Direction.LEFT, tolerance)) {
            collidedSide = Side.LEFT;
        } else if (Calculate.isEntityWithinToleranceOf(boundary1, boundary2, Direction.RIGHT, tolerance)) {
            collidedSide = Side.RIGHT;
        }
        if (Calculate.isEntityWithinToleranceOf(boundary1, boundary2, Direction.UP, tolerance)) {
            collidedSide = Side.TOP;
        } else if (Calculate.isEntityWithinToleranceOf(boundary1, boundary2, Direction.DOWN, tolerance)) {
            collidedSide = Side.BOTTOM;
        }
    }

}
