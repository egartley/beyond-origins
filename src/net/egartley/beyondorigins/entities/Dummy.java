package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Animation;
import net.egartley.beyondorigins.objects.Sprite;

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

    @Override
    public void tick() {
        isMovingUpwards = false;
        isMovingDownwards = false;
        isMovingLeftwards = false;
        isMovingRightwards = false;
        follow(Entities.PLAYER);
        super.tick();

        if (!isMovingRightwards && !isMovingLeftwards && !isMovingUpwards && !isMovingDownwards)
            animation.stop();
    }

    @Override
    public void onMove(byte direction) {
        if (animation.isStopped) {
            // animation was stopped, so restart it because we're moving
            animation.resume();
        }

        if (direction == RIGHT)
            switchAnimation(RIGHT_ANIMATION);
        else if (direction == LEFT)
            switchAnimation(LEFT_ANIMATION);
    }

    @Override
    public void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(4, 4, 2, 4)));
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
