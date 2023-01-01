package net.egartley.beyondorigins.engine.logic.combat;

import net.egartley.beyondorigins.engine.entities.CombatEntity;
import net.egartley.beyondorigins.engine.interfaces.Tickable;
import net.egartley.beyondorigins.entities.Entities;
import org.newdawn.slick.Animation;

public class Attack implements Tickable {

    protected final CombatEntity parent;

    public int damage, weight;
    public double cooldown;
    public boolean calledOnFinish;

    public Animation animation, animationLeft, animationRight;

    public Attack(CombatEntity parent, int damage, double cooldown, int weight,
                  Animation animationLeft, Animation animationRight) {
        this.parent = parent;
        this.damage = damage;
        this.cooldown = cooldown;
        this.weight = weight;
        this.animationLeft = animationLeft;
        this.animationRight = animationRight;
    }

    public void start() {
        calledOnFinish = false;
        animation.restart();
    }

    public void end() {
        Entities.PLAYER.dealDamage(damage);
        parent.onAttackFinish();
        calledOnFinish = true;
    }

    @Override
    public void tick() {
        if (animation.isStopped() && !calledOnFinish) {
            end();
        }
    }

}
