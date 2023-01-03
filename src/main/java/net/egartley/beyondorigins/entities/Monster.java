package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.entities.AnimatedEntity;
import net.egartley.beyondorigins.engine.enums.Direction;
import net.egartley.beyondorigins.engine.graphics.SpriteSheet;
import net.egartley.beyondorigins.engine.interfaces.Damageable;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.engine.threads.DelayedEvent;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.ingame.maps.testbattle.TestBattleMap;
import org.newdawn.slick.Animation;

/**
 * Test enemy
 */
public class Monster extends AnimatedEntity implements Damageable {

    private boolean isHurt;
    private final int ANIMATION_THRESHOLD = 200;
    private final byte LEFT_HURT_ANIMATION = 3;
    private final byte RIGHT_HURT_ANIMATION = 2;
    private final byte LEFT_NORMAL_ANIMATION = 1;
    private final byte RIGHT_NORMAL_ANIMATION = 0;
    private DelayedEvent hurtEvent = null;

    public Monster() {
        super("Monster", new SpriteSheet(Images.getImage(Images.MONSTER), 36, 58, 4, 3));
        isSectorSpecific = true;
        speed = 0.7;
        health = 35;
        maxHealth = health;
    }

    @Override
    public void onDeath() {
        onColdDeath();
        InGameState.map.sector.removeEntity(this);
        if (InGameState.map instanceof TestBattleMap tbm) {
            if (Util.fiftyFifty()) {
                tbm.spawnMonster(this.x + Util.randomInt(-25, 25), this.y + Util.randomInt(50, 75));
                if (Util.fiftyFifty()) {
                    tbm.spawnMonster(this.x + Util.randomInt(-25, 25), this.y - Util.randomInt(50, 75));
                }
            }
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
        boundaries.add(new EntityBoundary(this));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setCollisions() {
        Collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.attackBoundary));
    }

    @Override
    public void tick() {
        super.tick();
        if (!isMovingLeftwards && !isMovingRightwards) {
            isMovingRightwards = true;
            animation = animations.get(RIGHT_NORMAL_ANIMATION);
        }
        if (isMovingRightwards && x > Game.WINDOW_WIDTH - 75) {
            animation.stop();
            animation = isHurt ? animations.get(LEFT_NORMAL_ANIMATION) : animations.get(LEFT_HURT_ANIMATION);
            isMovingRightwards = false;
            isMovingLeftwards = true;
        } else if (isMovingLeftwards && x < 30) {
            animation.stop();
            animation = isHurt ? animations.get(RIGHT_NORMAL_ANIMATION) : animations.get(RIGHT_HURT_ANIMATION);
            isMovingLeftwards = false;
            isMovingRightwards = true;
        }
        if (isMovingRightwards) {
            move(Direction.RIGHT);
        } else {
            move(Direction.LEFT);
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
    public void dealDamage(int amount) {
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
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

}
