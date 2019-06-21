package net.egartley.beyondorigins.entities;

import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.interfaces.Collidable;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.objects.StaticEntity;

/**
 * Basic tree that the player can walk under, but not over
 */
public class DefaultTree extends StaticEntity implements Collidable {

    public DefaultTree(Sprite sprite, double x, double y) {
        super("Tree", sprite);
        this.x = x;
        this.y = y;
        setBoundaries();
        setCollisions();

        isSectorSpecific = true;
        isDualRendered = true;
        isTraversable = false;
        // set the first layer as the leaves
        firstLayer = image.getSubimage(0, 45, image.getWidth(), 19);
        // set the second layer as the trunk
        secondLayer = image.getSubimage(0, 0, image.getWidth(), 45);
    }

    @Override
    protected void setBoundaries() {
        defaultBoundary = new EntityBoundary(this, image.getWidth(), image.getHeight(),
                new BoundaryPadding(-24, -24, -24, -24));
        boundaries.add(defaultBoundary);
    }

    @Override
    public void setCollisions() {
        /*collisions = new ArrayList<>();
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
        }*/
    }

    @Override
    public void tick() {
        // collisions.forEach(EntityEntityCollision::tick);
    }

}
