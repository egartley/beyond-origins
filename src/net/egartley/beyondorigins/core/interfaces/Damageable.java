package net.egartley.beyondorigins.core.interfaces;

public interface Damageable {

    void takeDamage(int amount);

    void heal(int amount);

    void onDeath();

    void onColdDeath();

}
