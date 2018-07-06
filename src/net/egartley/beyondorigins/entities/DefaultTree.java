package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

import java.util.ArrayList;

/**
 * Basic tree that the player can walk under, but not over
 */
public class DefaultTree extends StaticEntity {

    private EntityBoundary boundary;

    public DefaultTree(Sprite sprite, double x, double y) {
        super("Tree", sprite);
        this.x = x;
        this.y = y;
        setBoundaries();
        setCollisions();

        isSectorSpecific = true;
        // set the first layer as the leaves
        firstLayer = image.getSubimage(0, 45, image.getWidth(), 19);
        // set the second layer as the trunk
        secondLayer = image.getSubimage(0, 0, image.getWidth(), 45);
    }

    /**
     * <p>
     * Disables the direction of movement opposite of where the player collided with the tree
     * </p>
     * <p>
     * <b>Example:</b> player collides with top of the tree, so downward movement is
     * disabled
     * </p>
     *
     * @param event
     *         The collision event between the player and tree
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
    protected void setBoundaries() {
        boundary = new EntityBoundary(this, image.getWidth(), image.getHeight(),
                new BoundaryPadding(-24, -24, -24, -24));
        boundaries.add(boundary);
    }

    @Override
    protected void setCollisions() {
        collisions = new ArrayList<>();
        EntityEntityCollision withPlayer = new EntityEntityCollision(Entities.PLAYER.headBoundary, boundary) {
            public void onCollide(EntityEntityCollisionEvent event) {
                onPlayerCollision(event);
            }

            public void onCollisionEnd(EntityEntityCollisionEvent event) {
                if (!Entities.PLAYER.isCollided)
                    Entities.PLAYER.allowAllMovement();
                else
                    Entities.PLAYER.annulCollisionEvent(event);
            }
        };
        collisions.add(withPlayer);

        for (EntityEntityCollision collision : Util.getAllBoundaryCollisions(withPlayer, Entities.PLAYER, boundary)) {
            if (collision.boundaries[0] != Entities.PLAYER.boundary)
                collisions.add(collision);
        }
    }

    @Override
    public void tick() {
        for (EntityEntityCollision collision : collisions) {
            collision.tick();
        }
    }

}
