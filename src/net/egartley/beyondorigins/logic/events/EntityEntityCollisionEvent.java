package net.egartley.beyondorigins.logic.events;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;

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

    /**
     * Creates a new entity-entity collision event, then determines {@link #collidedSide}
     *
     * @param invoker
     *         The collision that occurred
     */
    public EntityEntityCollisionEvent(EntityEntityCollision invoker) {
        // round player speed to int, ex. 1.6 would be 2
        // setting the tolerance based on player speed makes it so that regardless of the player speed, the collided
        // side should still be able to be determined
        int tolerance = (int) (Entities.PLAYER.speed + 0.5);

        EntityBoundary collider = invoker.boundary1;
        EntityBoundary into = invoker.boundary2;

        if (into.right - tolerance <= collider.left && collider.left <= into.right) {
            collidedSide = RIGHT_SIDE;
        } else if (into.left <= collider.right && collider.right <= into.left + tolerance) {
            collidedSide = LEFT_SIDE;
        } else if (into.top <= collider.bottom && collider.bottom <= into.top + tolerance) {
            collidedSide = TOP_SIDE;
        } else if (into.bottom - tolerance <= collider.top && collider.top <= into.bottom) {
            collidedSide = BOTTOM_SIDE;
        } else {
            Debug.error("Could not calculate a collided side! (between " + invoker.entities.get(0) + " and " +
                    invoker.entities.get(1) + ")");
        }
    }

}
