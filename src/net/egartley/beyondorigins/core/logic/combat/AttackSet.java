package net.egartley.beyondorigins.core.logic.combat;

import java.util.Arrays;
import java.util.ArrayList;

public class AttackSet {

    private final ArrayList<Attack> set;

    public AttackSet(Attack... attacks) {
        set = new ArrayList<>();
        set.addAll(Arrays.asList(attacks));
    }

    public Attack getNextAttack() {
        // in the future this will get a random attack from the set based on attack weights
        return set.get(0);
    }

}
