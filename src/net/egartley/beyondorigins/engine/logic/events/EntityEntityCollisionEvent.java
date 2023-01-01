package net.egartley.beyondorigins.engine.logic.events;

import net.egartley.beyondorigins.engine.enums.Direction;
import net.egartley.beyondorigins.engine.enums.Side;
import net.egartley.beyondorigins.engine.logic.Calculate;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
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
        if (Calculate.isEntityWithinTolerance(boundary1, boundary2, Direction.LEFT, tolerance)) {
            collidedSide = Side.LEFT;
        } else if (Calculate.isEntityWithinTolerance(boundary1, boundary2, Direction.RIGHT, tolerance)) {
            collidedSide = Side.RIGHT;
        }
        if (Calculate.isEntityWithinTolerance(boundary1, boundary2, Direction.UP, tolerance)) {
            collidedSide = Side.TOP;
        } else if (Calculate.isEntityWithinTolerance(boundary1, boundary2, Direction.DOWN, tolerance)) {
            collidedSide = Side.BOTTOM;
        }
    }

}
