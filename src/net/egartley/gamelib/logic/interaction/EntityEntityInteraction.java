package net.egartley.gamelib.logic.interaction;

import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;

/**
 * For now, just a collision between two entities
 */
public class EntityEntityInteraction {

    private boolean didDoInteraction;

    public Entity[] entities;
    private EntityEntityCollision collision;

    public EntityEntityInteraction(Entity e1, Entity e2) {
        entities = new Entity[]{e1, e2};
        collision = new EntityEntityCollision(e1.defaultBoundary, e2.defaultBoundary);
    }

    public void tick() {
        collision.tick();
        if (collision.isCollided && !didDoInteraction) {
            onInteraction();
            collision.end();
            didDoInteraction = true;
        } else if (!collision.isCollided) {
            didDoInteraction = false;
        }
    }

    public void onInteraction() {

    }

}
