package net.egartley.beyondorigins.logic.events;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.logic.math.Calculate;
import net.egartley.beyondorigins.objects.Entity;

/**
 * A custom "event" that can be used for gathering information from a collision that has occurred between two entities
 *
 * @see EntityEntityCollision
 */
public class EntityEntityCollisionEvent {

    public static final byte TOP_SIDE = 0;
    public static final byte LEFT_SIDE = 1;
    public static final byte BOTTOM_SIDE = 2;
    public static final byte RIGHT_SIDE = 3;

    /**
     * The numerical representation for the side that the collision occurred at
     *
     * @see #TOP_SIDE
     * @see #BOTTOM_SIDE
     * @see #LEFT_SIDE
     * @see #RIGHT_SIDE
     */
    public byte collidedSide = -1;

    public EntityEntityCollision invoker;

    /**
     * Creates a new entity-entity collision event, then determines {@link #collidedSide}
     *
     * @param invoker The collision that occurred
     */
    public EntityEntityCollisionEvent(EntityEntityCollision invoker) {
        this.invoker = invoker;
        int tolerance = (int) Entities.PLAYER.speed;
        EntityBoundary player = invoker.boundaries[0];
        EntityBoundary rock = invoker.boundaries[1];

        boolean top = Calculate.isEntityWithinToleranceOf(player, rock, Entity.UP, tolerance);
        boolean bottom = Calculate.isEntityWithinToleranceOf(player, rock, Entity.DOWN, tolerance);
        boolean left = Calculate.isEntityWithinToleranceOf(player, rock, Entity.LEFT, tolerance);
        boolean right = Calculate.isEntityWithinToleranceOf(player, rock, Entity.RIGHT, tolerance);

        if (left) {
            collidedSide = LEFT_SIDE;
        } else if (right) {
            collidedSide = RIGHT_SIDE;
        }
        if (top) {
            collidedSide = TOP_SIDE;
        } else if (bottom) {
            collidedSide = BOTTOM_SIDE;
        }
    }

}
