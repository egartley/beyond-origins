package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryOffset;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Animation;
import net.egartley.beyondorigins.objects.Sprite;

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
        setAnimationCollection();
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

    }

}
