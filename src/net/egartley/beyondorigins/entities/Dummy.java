package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.*;

import java.util.ArrayList;

public class Dummy extends AnimatedEntity {

    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final byte ANIMATION_THRESHOLD = 7;

    public Dummy(ArrayList<Sprite> sprites) {
        super("Dummy");
        this.sprites = sprites;
        sprite = sprites.get(0);
        x = 470.0;
        y = 240.0;
        setAnimations();
        setBoundaries();
        setCollisions();

        isSectorSpecific = false;
        isDualRendered = false;
        speed = 2.0;
    }

    private void switchAnimation(byte i) {
        if (i >= animations.size()) {
            Debug.warning("Tried to switch to an animation at an invalid index");
            return;
        }
        if (!animation.equals(animations.get(i))) {
            // this prevents the same animation being set again
            animation = animations.get(i);
        }
    }

    public void onSectorEnter(MapSector sector) {
        // generate collisions with sector entities that aren't traversable
        for (Entity e : sector.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                EntityEntityCollision baseCollision = new EntityEntityCollision(boundaries.get(0), e.defaultBoundary) {
                    public void onCollide(EntityEntityCollisionEvent event) {
                        onCollisionWithNonTraversableEntity(event);
                    }

                    public void onCollisionEnd(EntityEntityCollisionEvent event) {
                        if (!Entities.DUMMY.isCollided) {
                            allowAllMovement();
                        } else {
                            annulCollisionEvent(event);
                        }
                    }
                };
                collisions.add(baseCollision);
            }
        }
    }

    public void onSectorLeave(MapSector sector) {
        // remove the generated collisions

        // prevents concurrent modification
        ArrayList<EntityEntityCollision> removeCollisions = new ArrayList<>();

        for (Entity e : sector.entities)
            if (!e.isTraversable && e.isSectorSpecific)
                for (EntityEntityCollision c : collisions)
                    if (c.entities[0].equals(e) || c.entities[1].equals(e))
                        removeCollisions.add(c);

        for (EntityEntityCollision c : removeCollisions)
            collisions.remove(c);

        removeCollisions.clear();
    }

    @Override
    public void tick() {
        isMovingUpwards = false;
        isMovingDownwards = false;
        isMovingLeftwards = false;
        isMovingRightwards = false;
        follow(Entities.PLAYER, Entity.FOLLOW_AGGRESSIVE, 1);
        super.tick();

        if (!isMovingRightwards && !isMovingLeftwards && !isMovingUpwards && !isMovingDownwards)
            animation.stop();
    }

    @Override
    protected void onMove(byte direction) {
        if (animation.isStopped) {
            // animation was stopped, so restart it because we're moving
            animation.resume();
        }

        if (direction == RIGHT)
            switchAnimation(RIGHT_ANIMATION);
        else if (direction == LEFT)
            switchAnimation(LEFT_ANIMATION);
    }

    protected void onCaughtUp(byte direction) {
        animation.stop();
    }

    protected void onCaughtUpEnd(byte direction) {

    }

    @Override
    public void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(4, 4, 0, 4)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setAnimations() {
        animations.clear();
        animations.add(new Animation(sprites.get(0), ANIMATION_THRESHOLD));
        animations.add(new Animation(sprites.get(1), ANIMATION_THRESHOLD));
        animation = animations.get(0);
    }

    @Override
    protected void setCollisions() {

    }

}
