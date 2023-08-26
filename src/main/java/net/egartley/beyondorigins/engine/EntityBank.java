package net.egartley.beyondorigins.engine;

import java.util.ArrayList;

public class EntityBank {

    private static final ArrayList<Entity> entities = new ArrayList<>();

    public static void registerEntity(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);
        }
    }

    public static void removeEntity(Entity e) {
        entities.remove(e);
    }

    public static boolean isIDAvailable(int id) {
        for (Entity e : entities) {
            if (e.id == id) {
                return false;
            }
        }
        return true;
    }

    public static Entity getEntityByID(int id) {
        for (Entity e : entities) {
            if (e.id == id) {
                return e;
            }
        }
        System.out.println("WARNING: Could not find entity with id " + id);
        return null;
    }

    public static ArrayList<Entity> getEntities() {
        return entities;
    }

}
