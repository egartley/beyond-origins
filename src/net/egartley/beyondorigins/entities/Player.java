package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryOffset;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends AnimatedEntity {

    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final byte ANIMATION_THRESHOLD = 7;

    public EntityBoundary boundary;
    EntityBoundary headBoundary;
    EntityBoundary bodyBoundary;
    EntityBoundary feetBoundary;

    boolean isAllowedToMoveUpwards = true;
    boolean isAllowedToMoveDownwards = true;
    boolean isAllowedToMoveLeftwards = true;
    boolean isAllowedToMoveRightwards = true;

    public Player(ArrayList<Sprite> sprites) {
        super("Player");
        this.sprites = sprites;
        sprite = sprites.get(0);
        setAnimations();
        setBoundaries();
        setCollisions();

        isSectorSpecific = false;
        isDualRendered = false;
        speed = 2.0;
    }

    /**
     * Changes the animation to another one in the collection
     *
     * @param i The index of the animation
     */
    private void switchAnimation(byte i) {
        if (!animation.equals(animations.get(i))) {
            // this prevents the same animation being set again
            animation = animations.get(i);
        }
    }

    /**
     * Allows the player to move in all directions
     */
    void allowAllMovement() {
        isAllowedToMoveUpwards = true;
        isAllowedToMoveDownwards = true;
        isAllowedToMoveLeftwards = true;
        isAllowedToMoveRightwards = true;
    }

    /**
     * Cancels any movement restrictions imposed by the provided {@link net.egartley.beyondorigins.logic.events
     * .EntityEntityCollisionEvent EntityEntityCollisionEvent}
     *
     * @param event The {@link net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent EntityEntityCollisionEvent}
     *              in which to annul
     */
    private void annulCollisionEvent(EntityEntityCollisionEvent event) {
        // check for other movement restrictions
        for (EntityEntityCollision c : concurrentCollisions) {
            if (c.lastEvent.collidedSide == event.collidedSide && c.lastEvent.invoker != event.invoker) {
                // there is another collision that has the same movement restriction, so don't annul it
                return;
            }
        }

        switch (event.collidedSide) {
            case EntityEntityCollisionEvent.TOP_SIDE:
                isAllowedToMoveDownwards = true;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                isAllowedToMoveUpwards = true;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                isAllowedToMoveRightwards = true;
                break;
            case EntityEntityCollisionEvent.RIGHT_SIDE:
                isAllowedToMoveLeftwards = true;
                break;
            default:
                break;
        }
    }

    private void onCollisionWithNonTraversableEntity(EntityEntityCollisionEvent event) {
        lastCollisionEvent = event;
        switch (event.collidedSide) {
            case EntityEntityCollisionEvent.RIGHT_SIDE:
                // collided on the right, so disable leftwards movement
                isAllowedToMoveLeftwards = false;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                // collided on the left, so disable rightwards movement
                isAllowedToMoveRightwards = false;
                break;
            case EntityEntityCollisionEvent.TOP_SIDE:
                // collided at the top, so disable downwards movement
                isAllowedToMoveDownwards = false;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                // collided at the bottom, so disable upwards movement
                isAllowedToMoveUpwards = false;
                break;
            default:
                break;
        }
    }

    public void onSectorEnter(MapSector sector) {
        // generate collisions with sector entities that aren't traversable
        for (Entity e : sector.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                EntityEntityCollision baseCollision = new EntityEntityCollision(headBoundary, e.defaultBoundary) {
                    public void onCollide(EntityEntityCollisionEvent event) {
                        onCollisionWithNonTraversableEntity(event);
                    }

                    public void onCollisionEnd(EntityEntityCollisionEvent event) {
                        if (!Entities.PLAYER.isCollided) {
                            allowAllMovement();
                        } else {
                            annulCollisionEvent(event);
                        }
                    }
                };
                collisions.add(baseCollision);

                for (EntityEntityCollision collision : Util.getAllBoundaryCollisions(baseCollision, this, e.defaultBoundary)) {
                    if (collision.boundaries[0] != boundary && collision.boundaries[0] != headBoundary) {
                        collisions.add(collision);
                    }
                }
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
    public void setAnimations() {
        animations.clear();
        animations.add(new Animation(sprites.get(0), ANIMATION_THRESHOLD));
        animations.add(new Animation(sprites.get(1), ANIMATION_THRESHOLD));
        animation = animations.get(0);
    }

    @Override
    public void setBoundaries() {
        boundary = new EntityBoundary(this, sprite, new BoundaryPadding(4, 3, 2, 3));
        boundary.name = "Base";
        defaultBoundary = boundary;
        headBoundary = new EntityBoundary(this, 19, 18, new BoundaryPadding(0), new BoundaryOffset(0, 0, 0, 5));
        headBoundary.name = "Head";
        bodyBoundary = new EntityBoundary(this, 30, 19, new BoundaryPadding(0), new BoundaryOffset(0, 16, 0, 0));
        bodyBoundary.name = "Body";
        feetBoundary = new EntityBoundary(this, 17, 16, new BoundaryPadding(0), new BoundaryOffset(0, 29, 0, 6));
        feetBoundary.name = "Feet";

        boundaries.add(boundary);
        boundaries.add(headBoundary);
        boundaries.add(bodyBoundary);
        boundaries.add(feetBoundary);
    }

    @Override
    public void tick() {
        // get keyboard input (typical WASD)
        boolean up = Keyboard.isKeyPressed(KeyEvent.VK_W);
        boolean left = Keyboard.isKeyPressed(KeyEvent.VK_A);
        boolean down = Keyboard.isKeyPressed(KeyEvent.VK_S);
        boolean right = Keyboard.isKeyPressed(KeyEvent.VK_D);

        // actually move the player, with animations and all
        move(up, down, left, right);

        if (!left && !right && !down && !up) {
            // not moving, so stop the animation if not already
            animation.stop();
        }

        super.tick();
    }

    private void move(boolean up, boolean down, boolean left, boolean right) {
        if (animation.isStopped) {
            // animation was stopped, so restart it because we're moving
            animation.restart();
            animation.setFrame(1);
        }

        isMovingUpwards = false;
        isMovingDownwards = false;
        isMovingLeftwards = false;
        isMovingRightwards = false;
        if (up) {
            if (isAllowedToMoveUpwards) {
                move(UP);
            }
        } else if (down) {
            if (isAllowedToMoveDownwards) {
                move(DOWN);
            }
        }

        if (left) {
            if (isAllowedToMoveLeftwards) {
                move(LEFT);
            }
            switchAnimation(LEFT_ANIMATION);
        } else if (right) {
            if (isAllowedToMoveRightwards) {
                move(RIGHT);
            }
            switchAnimation(RIGHT_ANIMATION);
        }
    }

    @Override
    protected void setCollisions() {
        // see onSectorEnter and onSectorLeave
    }

}
