package net.egartley.beyondorigins.core.logic.collision;

import net.egartley.beyondorigins.core.abstracts.Entity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * A collection of all the collisions that can be checked (may not be active)
 */
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

    public static void removeAllWith(Entity entity) {
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

    public static void endAllWith(Entity entity) {
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

    public static void endAndRemove(EntityEntityCollision collision) {
        collision.end();
        remove(collision);
    }

    public static void endAndNuke() {
        for (EntityEntityCollision c : collisions) {
            c.end();
        }
        nuke();
    }

    public static void nuke() {
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

    public static int getAmount() {
        return collisions.size();
    }

    public static ArrayList<EntityEntityCollision> getAllWith(Entity entity) {
        ArrayList<EntityEntityCollision> with = new ArrayList<>();
        for (EntityEntityCollision c : collisions) {
            if (c.entities[0].equals(entity) || c.entities[1].equals(entity)) {
                with.add(c);
            }
        }
        return with;
    }

    public static ArrayList<EntityEntityCollision> getConcurrentWith(Entity entity) {
        ArrayList<EntityEntityCollision> concurrent = new ArrayList<>();
        for (EntityEntityCollision c : collisions) {
            if ((c.entities[0].equals(entity) || c.entities[1].equals(entity)) && c.isCollided) {
                concurrent.add(c);
            }
        }
        return concurrent;
    }

}
