package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
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

    private final int ANIMATION_THRESHOLD = 250;
    private final byte LEFT_NORMAL_ANIMATION = 0;
    private final byte RIGHT_NORMAL_ANIMATION = 1;

    public FH() {
        super("FH", new SpriteSheet(Images.get(Images.FH), 30, 44, 2, 4));
        isSectorSpecific = false;
        isDualRendered = false;
        speed = 0.6;
        health = 12;
        maximumHealth = health;
    }

    @Override
    public void tick() {
        super.tick();
        if (!isMovingLeftwards && !isMovingRightwards) {
            isMovingRightwards = true;
            animation = animations.get(RIGHT_NORMAL_ANIMATION);
        }
        if (isMovingRightwards && x() > Game.WINDOW_WIDTH - 75) {
            animation.stop();
            animation = animations.get(LEFT_NORMAL_ANIMATION);
            isMovingRightwards = false;
            isMovingLeftwards = true;
        } else if (isMovingLeftwards && x() < 30) {
            animation.stop();
            animation = animations.get(RIGHT_NORMAL_ANIMATION);
            isMovingLeftwards = false;
            isMovingRightwards = true;
        }
        if (isMovingRightwards) {
            move(DIRECTION_RIGHT);
        } else if (isMovingLeftwards) {
            move(DIRECTION_LEFT);
        }
        if (animation.isStopped()) {
            animation.start();
        }
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(RIGHT_NORMAL_ANIMATION)), ANIMATION_THRESHOLD));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(LEFT_NORMAL_ANIMATION)), ANIMATION_THRESHOLD));
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
