package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.combat.Attack;
import net.egartley.beyondorigins.core.logic.combat.AttackSet;
import net.egartley.beyondorigins.core.threads.DelayedEvent;
import net.egartley.beyondorigins.entities.Entities;

import java.util.ArrayList;

public abstract class BossEntity extends AnimatedEntity {

    public double spawnCooldown = 1.0D;
    public boolean spawnCooldownFinished, spawnCooldownStarted, inAttackCooldown, isAttacking;
    public Attack lastAttack;
    public AttackSet attackSet;
    public ArrayList<AttackSet> attackSets = new ArrayList<>();

    public BossEntity(String name, SpriteSheet... sheets) {
        super(name, sheets);
        setAttackSets();
    }

    public abstract void setAttackSets();

    public void startNextAttack() {
        System.out.println("Starting next attack");
        Attack attack = attackSet.getNextAttack();
        lastAttack = attack;
        isAttacking = true;
        attack.animation = this.isLeftOf(Entities.PLAYER) ? attack.animationRight : attack.animationLeft;
        switchAnimation(animations.indexOf(attack.animation));
        attack.start();
    }

    public void onAttackFinish() {
        System.out.println("Finished attack, starting cooldown");
        isAttacking = false;
        inAttackCooldown = true;
        new DelayedEvent(lastAttack.cooldown) {
            @Override
            public void onFinish() {
                System.out.println("Cooldown done");
                inAttackCooldown = false;
            }
        }.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (isAttacking) {
            lastAttack.tick();
        }
    }

}
