package net.egartley.gamelib.interfaces;

import net.egartley.gamelib.logic.interaction.EntityEntityInteraction;

import java.util.ArrayList;

public interface Interactable {

    ArrayList<EntityEntityInteraction> interactions = new ArrayList<>();

    void setInteractions();

    static void tick() {
        interactions.forEach(EntityEntityInteraction::tick);
    }

}
