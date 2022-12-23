package net.egartley.beyondorigins.entities.attacks;

import net.egartley.beyondorigins.core.abstracts.BossEntity;
import net.egartley.beyondorigins.core.logic.combat.Attack;
import net.egartley.beyondorigins.entities.Entities;
import org.newdawn.slick.Animation;

public class DoorSlamAttack extends Attack {

    public DoorSlamAttack(BossEntity parent, Animation animationLeft, Animation animationRight) {
        super(parent, 2, 0.8D, 1, animationLeft, animationRight);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        if (animation.isStopped() && !calledOnFinish) {
            System.out.println("Actually dealing damage");
            Entities.PLAYER.dealDamage(damage);
            parent.onAttackFinish();
            calledOnFinish = true;
        }
    }

}
