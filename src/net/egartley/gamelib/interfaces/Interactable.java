package net.egartley.gamelib.interfaces;

import net.egartley.gamelib.logic.interaction.EntityEntityInteraction;

import java.util.ArrayList;

public interface Interactable {

    ArrayList<EntityEntityInteraction> interactions = new ArrayList<>();

    void setInteractions();
    void hookInteraction(EntityEntityInteraction interaction);

}
