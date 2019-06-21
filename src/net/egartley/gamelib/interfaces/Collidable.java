package net.egartley.gamelib.interfaces;

import net.egartley.gamelib.logic.collision.EntityEntityCollision;

import java.util.ArrayList;

public interface Collidable {

    ArrayList<EntityEntityCollision> collisions = new ArrayList<>();
    ArrayList<EntityEntityCollision> concurrentCollisions = new ArrayList<>();

    void setCollisions();

    static void tick() {
        collisions.forEach(EntityEntityCollision::tick);
    }

}
