package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryOffset;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Animation;
import net.egartley.beyondorigins.objects.Sprite;

import java.awt.*;
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

    private boolean isMovingUpwards = false;
    private boolean isMovingDownwards = false;
    private boolean isMovingLeftwards = false;
    private boolean isMovingRightwards = false;

    public Player(ArrayList<Sprite> sprites) {
        super("Player");
        this.sprites = sprites;
        sprite = sprites.get(0);
        setAnimationCollection();
        setBoundaries();
        setCollisions();

        isSectorSpecific = false;
        isDualRendered = false;
        speed = 2;
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
    void annulCollisionEvent(EntityEntityCollisionEvent event) {
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

    /**
     * Returns whether or not the player is currently moving in a certain direction
     *
     * @param direction {@link #UP}, {@link #DOWN}, {@link #LEFT} or {@link #RIGHT}
     * @return True if the player is moving in the given direction, false if not or the given direction was unknown
     */
    public boolean isMoving(byte direction) {
        switch (direction) {
            case UP:
                return isMovingUpwards;
            case DOWN:
                return isMovingDownwards;
            case LEFT:
                return isMovingLeftwards;
            case RIGHT:
                return isMovingRightwards;
            default:
                Debug.warning("Tried to get an unknown movement from the player (" + direction + "), expected " + UP
                        + ", " + DOWN + ", " + LEFT + " or " + RIGHT + "");
                return false;
        }
    }

    @Override
    public void setAnimationCollection() {
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
    public void render(Graphics graphics) {
        animation.render(graphics, (int) x, (int) y);
        drawDebug(graphics);
    }

    @Override
    public void tick() {
        // get keyboard input (typical WASD)
        boolean up = Keyboard.isKeyPressed(KeyEvent.VK_W);
        boolean left = Keyboard.isKeyPressed(KeyEvent.VK_A);
        boolean down = Keyboard.isKeyPressed(KeyEvent.VK_S);
        boolean right = Keyboard.isKeyPressed(KeyEvent.VK_D);
        // reset all booleans for player's current movement
        isMovingUpwards = false;
        isMovingDownwards = false;
        isMovingLeftwards = false;
        isMovingRightwards = false;

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

        if (up) {
            isMovingUpwards = true;
            if (isAllowedToMoveUpwards) {
                move(UP, boundary);
            }
        } else if (down) {
            isMovingDownwards = true;
            if (isAllowedToMoveDownwards) {
                move(DOWN, boundary);
            }
        }

        if (left) {
            isMovingLeftwards = true;
            if (isAllowedToMoveLeftwards) {
                move(LEFT, boundary);
            }

            switchAnimation(LEFT_ANIMATION);
        } else if (right) {
            isMovingRightwards = true;
            if (isAllowedToMoveRightwards) {
                move(RIGHT, boundary);
            }

            switchAnimation(RIGHT_ANIMATION);
        }
    }

    @Override
    protected void setCollisions() {

    }

}
