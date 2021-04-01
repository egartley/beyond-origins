package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.AnimatedEntity;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.interfaces.Damageable;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.Animation;

/**
 * Test boss
 */
public class FH extends AnimatedEntity implements Damageable {

    private final int ANIMATION_THRESHOLD = 390;
    private final byte LEFT_NORMAL_ANIMATION = 0;
    private final byte RIGHT_NORMAL_ANIMATION = 1;

    public FH() {
        super("FH", new SpriteSheet(Images.get(Images.FH), 30, 44, 2, 4));
        isSectorSpecific = true;
        isDualRendered = false;
        speed = 0.35;
        health = 120;
        maximumHealth = health;
    }

    @Override
    public void tick() {
        super.tick();
        if (!isMovingLeftwards && !isMovingRightwards) {
            isMovingRightwards = true;
            animation = animations.get(RIGHT_NORMAL_ANIMATION);
        }
        if (isMovingRightwards && this.isRightOf(Entities.PLAYER)) {
            animation.stop();
            animation = animations.get(LEFT_NORMAL_ANIMATION);
            isMovingRightwards = false;
            isMovingLeftwards = true;
        } else if (isMovingLeftwards && this.isLeftOf(Entities.PLAYER)) {
            animation.stop();
            animation = animations.get(RIGHT_NORMAL_ANIMATION);
            isMovingLeftwards = false;
            isMovingRightwards = true;
        }
        if (this.isBelow(Entities.PLAYER)) {
            isMovingDownwards = false;
            isMovingUpwards = true;
        } else if (this.isAbove(Entities.PLAYER)) {
            isMovingDownwards = true;
            isMovingUpwards = false;
        }
        if (isMovingRightwards) {
            move(DIRECTION_RIGHT, defaultBoundary, false);
        } else if (isMovingLeftwards) {
            move(DIRECTION_LEFT, defaultBoundary, false);
        }
        if (isMovingDownwards) {
            move(DIRECTION_DOWN, defaultBoundary, false);
        } else if (isMovingUpwards) {
            move(DIRECTION_UP, defaultBoundary, false);
        }
        if (animation.isStopped()) {
            animation.start();
        }
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(LEFT_NORMAL_ANIMATION)), ANIMATION_THRESHOLD));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(RIGHT_NORMAL_ANIMATION)), ANIMATION_THRESHOLD));
        animation = animations.get(RIGHT_NORMAL_ANIMATION);
    }

    @Override
    protected void setCollisions() {
        Collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.attackBoundary));
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(0, 2, 0, 2)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    protected void setInteractions() {

    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
        if (health == 0) {
            onDeath();
        }
    }

    @Override
    public void heal(int amount) {
        health += amount;
        if (health > maximumHealth) {
            health = maximumHealth;
        }
    }

    @Override
    public void onDeath() {
        onColdDeath();
        InGameState.map.sector.removeEntity(this);
    }

    @Override
    public void onColdDeath() {
        animation.stop();
    }

}
