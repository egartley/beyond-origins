package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.graphics.Sprite;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
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
        isTraversable = false;
        // top half
        firstLayer = image.getSubimage(0, image.getHeight() / 2, image.getWidth(), image.getHeight() / 2);
        // bottom half
        secondLayer = image.getSubimage(0, 0, image.getWidth(), image.getHeight() / 2);
    }

    @Override
    public void tick() {
        // collisions.forEach(EntityEntityCollision::tick);
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, image.getWidth(), image.getHeight(),
                new BoundaryPadding(-4, 1, -8, 1)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    protected void setCollisions() {
        /*EntityEntityCollision withPlayer = new EntityEntityCollision(Entities.PLAYER.headBoundary, boundaries.get(0)) {
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
        }*/
    }

}
