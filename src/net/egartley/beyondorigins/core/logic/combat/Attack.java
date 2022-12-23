package net.egartley.beyondorigins.core.logic.combat;

import net.egartley.beyondorigins.core.abstracts.BossEntity;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import org.newdawn.slick.Animation;

public class Attack implements Tickable {

    protected final BossEntity parent;

    public int damage, weight;
    public double cooldown;
    public boolean calledOnFinish;

    public Animation animation, animationLeft, animationRight;

    public Attack(BossEntity parent, int damage, double cooldown, int weight,
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

    @Override
    public void tick() {
        if (animation.isStopped() && !calledOnFinish) {
            parent.onAttackFinish();
            calledOnFinish = true;
        }
    }

}
