package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.abstracts.AnimatedEntity;
import net.egartley.beyondorigins.core.graphics.Animation;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.data.Images;

public class Monster extends AnimatedEntity {

    private final byte LEFT_ANIMATION = 1;
    private final byte RIGHT_ANIMATION = 0;
    private final int ANIMATION_THRESHOLD = 225;

    boolean switchAnimation;

    public Monster() {
        super("Monster", new SpriteSheet(Images.get(Images.MONSTER), 36, 58, 4, 3));

        isSectorSpecific = true;
        isDualRendered = false;
        speed = 0.6;
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(sprites.get(RIGHT_ANIMATION), ANIMATION_THRESHOLD));
        animations.add(new Animation(sprites.get(LEFT_ANIMATION), ANIMATION_THRESHOLD));
        animation = animations.get(RIGHT_ANIMATION);
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(0, 2, 0, 2)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    protected void setCollisions() {

    }

    @Override
    protected void setInteractions() {

    }

    @Override
    public void tick() {
        super.tick();
        if (!isMovingLeftwards && !isMovingRightwards) {
            isMovingRightwards = true;
            animation = animations.get(RIGHT_ANIMATION);
        }
        if (isMovingRightwards && x() > 400) {
            animation.stop();
            animation = animations.get(LEFT_ANIMATION);
            isMovingRightwards = false;
            isMovingLeftwards = true;
        } else if (isMovingLeftwards && x() < 50) {
            animation.stop();
            animation = animations.get(RIGHT_ANIMATION);
            isMovingLeftwards = false;
            isMovingRightwards = true;
        }
        if (isMovingRightwards) {
            move(DIRECTION_RIGHT);
        } else if (isMovingLeftwards) {
            move(DIRECTION_LEFT);
        }
        if (!animation.isRunning()) {
            animation.start();
        }
    }

}
