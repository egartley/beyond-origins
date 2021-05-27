package net.egartley.beyondorigins.entities;

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
import org.newdawn.slick.Animation;

/**
 * Test boss. Inspired by a real person
 */
public class FH extends AnimatedEntity implements Damageable {

    private boolean readyToHeal = true;
    private final int ANIMATION_THRESHOLD = 390;
    private final int REGEN_AMOUNT = 2;
    private final byte LEFT_NORMAL_ANIMATION = 0;
    private final byte RIGHT_NORMAL_ANIMATION = 1;
    private final double REGEN_DELAY = 1.25D;

    public FH() {
        super("FH", new SpriteSheet(Images.getImage(Images.FH), 30, 44, 2, 4));
        isSectorSpecific = true;
        isDualRendered = false;
        speed = 0.35;
        health = 120;
        maxHealth = health;
    }

    @Override
    public void tick() {
        super.tick();

        // regenerate health
        if (health < maxHealth && readyToHeal) {
            readyToHeal = false;
            new DelayedEvent(REGEN_DELAY) {
                @Override
                public void onFinish() {
                    heal(REGEN_AMOUNT);
                    readyToHeal = true;
                }
            }.start();
        }

        // follow/catch up with player
        if (!isMovingLeftwards && !isMovingRightwards) {
            isMovingRightwards = true;
            animation = animations.get(RIGHT_NORMAL_ANIMATION);
        }
        follow(Entities.PLAYER, defaultBoundary, (int) Math.ceil(speed));

        if (animation.isStopped()) {
            animation.start();
        }
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(LEFT_NORMAL_ANIMATION)), ANIMATION_THRESHOLD));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(RIGHT_NORMAL_ANIMATION)), ANIMATION_THRESHOLD));
        animation = animations.get(RIGHT_NORMAL_ANIMATION);
        leftAnimationIndex = 0;
        rightAnimationIndex = 1;
    }

    @Override
    protected void setCollisions() {
        Collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.attackBoundary));
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(0, 2, 0, 2)));
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
        if (health > maxHealth) {
            health = maxHealth;
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
