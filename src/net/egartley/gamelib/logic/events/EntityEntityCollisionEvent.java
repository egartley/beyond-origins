package net.egartley.gamelib.logic.events;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.logic.math.Calculate;

/**
 * A "event" that can be used for accessing information about an {@link EntityEntityCollision}
 */
public class EntityEntityCollisionEvent {

    public static final byte TOP_SIDE = 0;
    public static final byte LEFT_SIDE = 1;
    public static final byte BOTTOM_SIDE = 2;
    public static final byte RIGHT_SIDE = 3;

    /**
     * The side that the collision occurred at
     *
     * @see #TOP_SIDE
     * @see #BOTTOM_SIDE
     * @see #LEFT_SIDE
     * @see #RIGHT_SIDE
     */
    public byte collidedSide = -1;
    /**
     * The collision that caused the event
     */
    public EntityEntityCollision invoker;

    /**
     * Creates a new entity-entity collision event, which calculates its {@link #collidedSide}
     */
    public EntityEntityCollisionEvent(EntityEntityCollision invoker) {
        this.invoker = invoker;
        // calculate the side in which the collision occurred
        calculateCollidedSide((int) Entities.PLAYER.speed, invoker.boundaries[0], invoker.boundaries[1]);
    }

    private void calculateCollidedSide(int tolerance, EntityBoundary player, EntityBoundary rock) {
        // Debug.out("Calculating collided side for " + player.parent + " and " + rock.parent + ", " + tolerance);
        if (Calculate.isEntityWithinToleranceOf(player, rock, Entity.DIRECTION_LEFT, tolerance)) {
            collidedSide = LEFT_SIDE;
        } else if (Calculate.isEntityWithinToleranceOf(player, rock, Entity.DIRECTION_RIGHT, tolerance)) {
            collidedSide = RIGHT_SIDE;
        }
        if (Calculate.isEntityWithinToleranceOf(player, rock, Entity.DIRECTION_UP, tolerance)) {
            collidedSide = TOP_SIDE;
        } else if (Calculate.isEntityWithinToleranceOf(player, rock, Entity.DIRECTION_DOWN, tolerance)) {
            collidedSide = BOTTOM_SIDE;
        }
        // Debug.out("Calculated");
    }

}
