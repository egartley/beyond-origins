package net.egartley.beyondorigins.core.logic.collision;

import net.egartley.beyondorigins.core.abstracts.Entity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Collisions {

    private static final ArrayList<EntityEntityCollision> collisions = new ArrayList<>();

    public static void add(EntityEntityCollision collision) {
        if (!collisions.contains(collision)) {
            collisions.add(collision);
        }
    }

    public static void remove(EntityEntityCollision collision) {
        collisions.remove(collision);
    }

    public static void removeWith(Entity entity) {
        ArrayList<EntityEntityCollision> toRemove = new ArrayList<>();
        for (EntityEntityCollision c : collisions) {
            if (c.entities[0].equals(entity) || c.entities[1].equals(entity)) {
                toRemove.add(c);
            }
        }
        for (EntityEntityCollision c : toRemove) {
            collisions.remove(c);
        }
    }

    public static void endWith(Entity entity) {
        ArrayList<EntityEntityCollision> end = new ArrayList<>();
        for (EntityEntityCollision c : collisions) {
            if (c.entities[0].equals(entity) || c.entities[1].equals(entity)) {
                end.add(c);
            }
        }
        for (EntityEntityCollision c : end) {
            c.end();
        }
    }

    public static ArrayList<EntityEntityCollision> with(Entity entity) {
        ArrayList<EntityEntityCollision> with = new ArrayList<>();
        for (EntityEntityCollision c : collisions) {
            if (c.entities[0].equals(entity) || c.entities[1].equals(entity)) {
                with.add(c);
            }
        }
        return with;
    }

    public static ArrayList<EntityEntityCollision> all() {
        return collisions;
    }

    public static void clear() {
        collisions.clear();
    }

    public static void tick() {
        try {
            for (EntityEntityCollision c : collisions) {
                c.tick();
            }
        } catch (ConcurrentModificationException e) {
            // ignore
        }
    }

    public static int amount() {
        return collisions.size();
    }

}
