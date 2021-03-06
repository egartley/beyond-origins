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
import net.egartley.beyondorigins.core.threads.DelayedEvent;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.ingame.maps.testbattle.TestBattleMap;
import org.newdawn.slick.Animation;

/**
 * Test enemy
 */
public class Monster extends AnimatedEntity implements Damageable {

    private boolean isHurt;
    private DelayedEvent hurtEvent = null;
    private final int ANIMATION_THRESHOLD = 200;
    private final byte LEFT_HURT_ANIMATION = 3;
    private final byte RIGHT_HURT_ANIMATION = 2;
    private final byte LEFT_NORMAL_ANIMATION = 1;
    private final byte RIGHT_NORMAL_ANIMATION = 0;

    public Monster() {
        super("Monster", new SpriteSheet(Images.get(Images.MONSTER), 36, 58, 4, 3));
        isSectorSpecific = true;
        isDualRendered = false;
        speed = 0.7;
        health = 35;
        maximumHealth = health;
    }

    @Override
    public void onDeath() {
        onColdDeath();
        InGameState.map.sector.removeEntity(this);
        if (InGameState.map instanceof TestBattleMap) {
            ((TestBattleMap) (InGameState.map)).spawnMonster(this.x() + Util.randomInt(-25, 25), this.y() + Util.randomInt(50, 75));
            ((TestBattleMap) (InGameState.map)).spawnMonster(this.x() + Util.randomInt(-25, 25), this.y() - Util.randomInt(50, 75));
        }
    }

    @Override
    public void onColdDeath() {
        animation.stop();
        if (hurtEvent != null) {
            hurtEvent.cancel();
        }
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(RIGHT_NORMAL_ANIMATION)), ANIMATION_THRESHOLD));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(LEFT_NORMAL_ANIMATION)), ANIMATION_THRESHOLD));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(RIGHT_HURT_ANIMATION)), ANIMATION_THRESHOLD));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(LEFT_HURT_ANIMATION)), ANIMATION_THRESHOLD));
        animation = animations.get(RIGHT_NORMAL_ANIMATION);
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(0, 2, 0, 2)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setCollisions() {
        Collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.attackBoundary));
    }

    @Override
    protected void setInteractions() {

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
            animation = isHurt ? animations.get(LEFT_NORMAL_ANIMATION) : animations.get(LEFT_HURT_ANIMATION);
            isMovingRightwards = false;
            isMovingLeftwards = true;
        } else if (isMovingLeftwards && x() < 30) {
            animation.stop();
            animation = isHurt ? animations.get(RIGHT_HURT_ANIMATION) : animations.get(RIGHT_HURT_ANIMATION);
            isMovingLeftwards = false;
            isMovingRightwards = true;
        }
        if (isMovingRightwards) {
            move(DIRECTION_RIGHT);
        } else if (isMovingLeftwards) {
            move(DIRECTION_LEFT);
        }
        if (isHurt && (animations.indexOf(animation) == RIGHT_NORMAL_ANIMATION || animations.indexOf(animation) == LEFT_NORMAL_ANIMATION)) {
            int oldFrameIndex = animation.getFrame();
            animation = animations.indexOf(animation) == RIGHT_NORMAL_ANIMATION ? animations.get(RIGHT_HURT_ANIMATION) : animations.get(LEFT_HURT_ANIMATION);
            animation.setCurrentFrame(oldFrameIndex);
        } else if (!isHurt && (animations.indexOf(animation) == RIGHT_HURT_ANIMATION || animations.indexOf(animation) == LEFT_HURT_ANIMATION)) {
            int oldFrameIndex = animation.getFrame();
            animation = animations.indexOf(animation) == RIGHT_HURT_ANIMATION ? animations.get(RIGHT_NORMAL_ANIMATION) : animations.get(LEFT_NORMAL_ANIMATION);
            animation.setCurrentFrame(oldFrameIndex);
        }
        if (animation.isStopped()) {
            animation.start();
        }
    }

    @Override
    public void inflict(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
        if (health == 0) {
            onDeath();
        }
        if (hurtEvent == null) {
            isHurt = true;
            hurtEvent = new DelayedEvent(0.25) {
                @Override
                public void onFinish() {
                    isHurt = false;
                    hurtEvent = null;
                }
            };
            hurtEvent.start();
        }
    }

    @Override
    public void heal(int amount) {
        health += amount;
        if (health > maximumHealth) {
            health = maximumHealth;
        }
    }

}
