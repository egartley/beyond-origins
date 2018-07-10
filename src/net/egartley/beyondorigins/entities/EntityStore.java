package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.objects.Entity;

import java.util.ArrayList;

/**
 * Storage for all the entities that are currently in use
 *
 * @see Entity
 */
public class EntityStore {

    /**
     * Internal store of entities
     */
    private static ArrayList<Entity> entities = new ArrayList<>();
    /**
     * The number of currently registered entities
     */
    public static int amount = 0;

    /**
     * Addes the given entity to the store. Prints out a warning if it has already been registered
     *
     * @param entity
     *         The entity to add to the store
     */
    public static void register(Entity entity) {
        if (!entity.isRegistered) {
            entities.add(entity);
            entity.isRegistered = true;
            amount++;
        } else {
            Debug.warning("Tried to add an entity (" + entity + ") to the store, but it has already been registered");
        }
    }

    /**
     * Removes the given entity from the registry. Prints out a warning if the entity was never registered in the first
     * place
     *
     * @param entity
     *         Entity to remove
     */
    public static void remove(Entity entity) {
        if (entity.isRegistered) {
            entities.remove(entity);
            entity.isRegistered = false;
            amount--;
        }
    }

    /**
     * Returns the entity that has the given UUID, null if no entity with that particular UUID is registered
     *
     * @param uuid
     *         UUID number to look for in the store
     *
     * @return Entity with the provided UUID, null if not found
     */
    public static Entity getEntityByUUID(int uuid) {
        for (Entity e : entities) {
            if (e.uuid == uuid)
                return e;
        }
        return null;
    }

}
