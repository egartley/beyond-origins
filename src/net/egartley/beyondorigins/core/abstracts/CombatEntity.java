package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.combat.Attack;
import net.egartley.beyondorigins.core.logic.combat.AttackSet;
import net.egartley.beyondorigins.core.threads.DelayedEvent;
import net.egartley.beyondorigins.entities.Entities;

import java.util.ArrayList;

public abstract class CombatEntity extends AnimatedEntity {

    public double spawnCooldown = 1.0D;
    public boolean spawnCooldownFinished, spawnCooldownStarted, inAttackCooldown, isAttacking;
    public Attack currentAttack;
    public AttackSet attackSet;
    public ArrayList<AttackSet> attackSets = new ArrayList<>();

    public CombatEntity(String name, SpriteSheet... sheets) {
        super(name, sheets);
        setAttackSets();
    }

    public abstract void setAttackSets();

    public void startNextAttack() {
        Attack attack = attackSet.getNextAttack();
        currentAttack = attack;
        isAttacking = true;
        attack.animation = this.isLeftOf(Entities.PLAYER) ? attack.animationRight : attack.animationLeft;
        switchAnimation(animations.indexOf(attack.animation));
        attack.start();
    }

    public void onAttackFinish() {
        isAttacking = false;
        inAttackCooldown = true;
        new DelayedEvent(currentAttack.cooldown) {
            @Override
            public void onFinish() {
                inAttackCooldown = false;
            }
        }.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (isAttacking) {
            currentAttack.tick();
        }
    }

}
