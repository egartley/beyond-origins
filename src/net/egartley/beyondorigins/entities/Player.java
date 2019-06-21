package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.gamelib.graphics.Animation;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.input.Keyboard;
import net.egartley.gamelib.interfaces.Character;
import net.egartley.gamelib.interfaces.Collidable;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;
import net.egartley.gamelib.logic.interaction.BoundaryOffset;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.objects.AnimatedEntity;
import net.egartley.gamelib.objects.Entity;
import net.egartley.gamelib.objects.MapSector;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends AnimatedEntity implements Character, Collidable {

    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final int ANIMATION_THRESHOLD = 135;

    public EntityBoundary boundary;
    EntityBoundary headBoundary;
    EntityBoundary bodyBoundary;
    EntityBoundary feetBoundary;

    public Player(ArrayList<Sprite> sprites) {
        super("Player");
        this.sprites = sprites;
        sprite = sprites.get(0);
        setAnimations();
        setBoundaries();
        setCollisions();

        isSectorSpecific = false;
        isDualRendered = false;
        speed = 1.1;
        if (Game.debug) {
            speed = 2.0;
        }
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

    public void onSectorEnter(MapSector sector) {
        // generate collisions with sector entities that aren't traversable
        for (Entity e : sector.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                EntityEntityCollision baseCollision = new EntityEntityCollision(headBoundary, e.defaultBoundary) {
                    public void onCollide(EntityEntityCollisionEvent event) {
                        Util.onCollisionWithNonTraversableEntity(event, Entities.PLAYER);
                    }

                    public void onCollisionEnd(EntityEntityCollisionEvent event) {
                        boolean noMovementRestrictions = true;
                        for (EntityEntityCollision c : Entities.PLAYER.concurrentCollisions) {
                            if (c.isMovementRestricting) {
                                noMovementRestrictions = false;
                                break;
                            }
                        }
                        if (!Entities.PLAYER.isCollided || noMovementRestrictions) {
                            allowAllMovement();
                        } else {
                            Util.annulCollisionEvent(event, Entities.PLAYER);
                        }
                    }
                };
                baseCollision.isMovementRestricting = true;
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
        bodyBoundary = new EntityBoundary(this, 30, 17, new BoundaryPadding(0), new BoundaryOffset(0, 18, 0, 0));
        bodyBoundary.name = "Body";
        feetBoundary = new EntityBoundary(this, 17, 16, new BoundaryPadding(0), new BoundaryOffset(0, 29, 0, 6));
        feetBoundary.name = "Feet";

        boundaries.add(boundary);
        boundaries.add(headBoundary);
        boundaries.add(bodyBoundary);
        boundaries.add(feetBoundary);
    }

    @Override
    public void setCollisions() {

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
        Collidable.tick();
    }

    public void render(Graphics graphics) {
        super.render(graphics);
    }

    private void move(boolean up, boolean down, boolean left, boolean right) {
        if (!animation.clock.isRunning) {
            // animation was stopped, so restart it because we're moving
            animation.start();
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

}
