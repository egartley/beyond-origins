package net.egartley.beyondorigins.entities.attacks;

import net.egartley.beyondorigins.core.abstracts.CombatEntity;
import net.egartley.beyondorigins.core.logic.combat.Attack;
import net.egartley.beyondorigins.entities.Entities;
import org.newdawn.slick.Animation;

public class DoorSlamAttack extends Attack {

    public DoorSlamAttack(CombatEntity parent, Animation animationLeft, Animation animationRight) {
        super(parent, 2, 1.2D, 1, animationLeft, animationRight);
    }

    public void end() {
        super.end();
        parent.setSprites(0);
    }

}
