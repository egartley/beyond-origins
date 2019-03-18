package net.egartley.beyondorigins.interfaces;

import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;

import java.util.ArrayList;

public interface Collidable {

    ArrayList<EntityEntityCollision> collisions = new ArrayList<>();
    ArrayList<EntityEntityCollision> concurrentCollisions = new ArrayList<>();

    void setCollisions();

    static void tick() {
        collisions.forEach(EntityEntityCollision::tick);
    }

}
