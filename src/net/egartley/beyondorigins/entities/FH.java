package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.entities.CombatEntity;
import net.egartley.beyondorigins.engine.graphics.SpriteSheet;
import net.egartley.beyondorigins.engine.interfaces.Damageable;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.engine.logic.combat.AttackSet;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.BoundaryPadding;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.engine.threads.DelayedEvent;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.attacks.DoorSlamAttack;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.Animation;

/**
 * Test boss. Inspired by a real person
 */
public class FH extends CombatEntity implements Damageable {

    private boolean readyToHeal = true;
    private final int ANIMATION_THRESHOLD = 390, ATTACK_THRESHOLD = 150, REGEN_AMOUNT = 2;
    private final double REGEN_DELAY = 1.25D;

    private AttackSet doorSlamAttackSet;

    public FH() {
        super("FH",
                new SpriteSheet(Images.getImage(Images.FH_WALK), 30, 44, 2, 4),
                new SpriteSheet(Images.getImage(Images.FH_ATTACK1), 30, 44, 2, 4));
        spawnCooldown = 2.0D;
        isSectorSpecific = true;
        isDualRendered = false;
        speed = 0.35;
        health = 120;
        maxHealth = health;
    }

    @Override
    public void tick() {
        super.tick();

        // spawn cooldown
        if (!spawnCooldownStarted) {
            // normally would start an idle or spawn animation here
            // for now just do nothing
            spawnCooldownStarted = true;
            new DelayedEvent(spawnCooldown) {
                @Override
                public void onFinish() {
                    spawnCooldownFinished = true;
                }
            }.start();
            return;
        }

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

        if (!spawnCooldownFinished) {
            return;
        }

        if (!inAttackCooldown && !isAttacking) {
            startNextAttack();
            setSprites(1);
        } else if (inAttackCooldown) {
            // follow/catch up with player
            if (!isMovingLeftwards && !isMovingRightwards) {
                isMovingRightwards = true;
                animation = animations.get(rightAnimationIndex);
            }
            follow(Entities.PLAYER, defaultBoundary, (int) Math.ceil(speed));
            if (animation.isStopped()) {
                animation.start();
            }
        }
    }

    @Override
    public void setAttackSets() {
        DoorSlamAttack doorSlamAttack = new DoorSlamAttack(this, animations.get(2), animations.get(3));
        doorSlamAttackSet = new AttackSet(doorSlamAttack);
        attackSets.add(doorSlamAttackSet);
        attackSet = doorSlamAttackSet;
    }

    @Override
    public void setAnimations() {
        leftAnimationIndex = 0;
        rightAnimationIndex = 1;
        animations.add(
                new Animation(Util.getAnimationFrames(sprites.get(leftAnimationIndex)), ANIMATION_THRESHOLD));
        animations.add(
                new Animation(Util.getAnimationFrames(sprites.get(rightAnimationIndex)), ANIMATION_THRESHOLD));
        animation = animations.get(rightAnimationIndex);

        SpriteSheet attack1Sheet = sheets.get(1);
        Animation attack1AnimationLeft = new Animation(Util.getAnimationFrames(attack1Sheet.sprites.get(0)), ATTACK_THRESHOLD);
        attack1AnimationLeft.setLooping(false);
        Animation attack1AnimationRight = new Animation(Util.getAnimationFrames(attack1Sheet.sprites.get(1)), ATTACK_THRESHOLD);
        attack1AnimationRight.setLooping(false);
        animations.add(attack1AnimationLeft);
        animations.add(attack1AnimationRight);
    }

    @Override
    protected void setCollisions() {
        Collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.attackBoundary));
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite.width, sprite.height,
                new BoundaryPadding(0, 2, 0, 2)));
        defaultBoundary = boundaries.get(0);
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
        animation.stop();
        InGameState.map.sector.removeEntity(this);
    }

    @Override
    public void onColdDeath() {
        animation.stop();
    }

}
