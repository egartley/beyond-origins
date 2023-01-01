package net.egartley.beyondorigins.engine.interfaces;

public interface Damageable {

    void dealDamage(int amount);

    void heal(int amount);

    void onDeath();

    void onColdDeath();

}
