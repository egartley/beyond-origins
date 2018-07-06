package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

/**
 * Rock that the player cannot walk over, but can walk behind
 */
public class DefaultRock extends StaticEntity {

    public DefaultRock(Sprite sprite, double x, double y) {
        super("Rock", sprite);
        this.x = x;
        this.y = y;
        setBoundaries();
        setCollisions();

        isSectorSpecific = true;
        isDualRendered = true;
        // top half
        firstLayer = image.getSubimage(0, image.getHeight() / 2, image.getWidth(), image.getHeight() / 2);
        // bottom half
        secondLayer = image.getSubimage(0, 0, image.getWidth(), image.getHeight() / 2);
    }

    /**
     * <p>
     * Disables the direction of movement opposite of where the player collided with the rock
     * </p>
     * <p>
     * <b>Example:</b> player collides with top of the rock, so downward movement is
     * disabled
     * </p>
     *
     * @param event
     *         The collision event between the player and rock
     */
    private void onPlayerCollision(EntityEntityCollisionEvent event) {
        Entities.PLAYER.lastCollisionEvent = event;
        switch (event.collidedSide) {
            case EntityEntityCollisionEvent.RIGHT_SIDE:
                // collided on the right, so disable leftwards movement
                Entities.PLAYER.isAllowedToMoveLeftwards = false;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                // collided on the left, so disable rightwards movement
                Entities.PLAYER.isAllowedToMoveRightwards = false;
                break;
            case EntityEntityCollisionEvent.TOP_SIDE:
                // collided at the top, so disable downwards movement
                Entities.PLAYER.isAllowedToMoveDownwards = false;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                // collided at the bottom, so disable upwards movement
                Entities.PLAYER.isAllowedToMoveUpwards = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void tick() {
        collisions.forEach(EntityEntityCollision::tick);
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, image.getWidth(), image.getHeight(),
                new BoundaryPadding(-4, 1, -8, 1)));
    }

    @Override
    protected void setCollisions() {
        EntityEntityCollision withPlayer = new EntityEntityCollision(Entities.PLAYER.headBoundary, boundaries.get(0)) {
            public void onCollide(EntityEntityCollisionEvent event) {
                onPlayerCollision(event);
            }

            public void onCollisionEnd(EntityEntityCollisionEvent event) {
                if (!Entities.PLAYER.isCollided) {
                    Entities.PLAYER.allowAllMovement();
                } else {
                    Entities.PLAYER.annulCollisionEvent(event);
                }
            }
        };
        collisions.add(withPlayer);

        for (EntityEntityCollision collision : Util.getAllBoundaryCollisions(withPlayer, Entities.PLAYER, boundaries
                .get(0))) {
            if (collision.boundaries[0] != Entities.PLAYER.boundary && collision.boundaries[0] != Entities.PLAYER
                    .headBoundary) {
                collisions.add(collision);
            }
        }
    }

}
