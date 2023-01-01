package net.egartley.beyondorigins.engine.logic.collision;

import net.egartley.beyondorigins.engine.entities.Entity;
import net.egartley.beyondorigins.engine.controllers.KeyboardController;
import net.egartley.beyondorigins.engine.input.KeyTyped;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.engine.logic.events.EntityEntityCollisionEvent;
import org.newdawn.slick.Input;

/**
 * An interaction between two entities, usually with the player as one of them
 */
public class EntityEntityInteraction {

    public Entity[] entities;
    public EntityEntityCollision collision;

    private boolean isActive;
    private boolean didInteract;
    private static final int DEFAULT_KEY_CODE = Input.KEY_ENTER;
    private final KeyTyped keyTyped;

    /**
     * Creates a new entity-to-entity interaction based on both of their default boundaries
     */
    public EntityEntityInteraction(Entity e1, Entity e2) {
        this(e1.defaultBoundary, e2.defaultBoundary);
    }

    public EntityEntityInteraction(EntityBoundary b1, EntityBoundary b2) {
        entities = new Entity[]{b1.entity, b2.entity};
        collision = new EntityEntityCollision(b1, b2) {
            @Override
            public void end(EntityEntityCollisionEvent event) {
                didInteract = false;
            }
        };
        keyTyped = new KeyTyped(DEFAULT_KEY_CODE) {
            @Override
            public void onType() {
                if (collision.isCollided && !didInteract) {
                    interact();
                    didInteract = true;
                }
            }
        };
    }

    public void tick() {
        if (!isActive) {
            return;
        }
        collision.tick();
    }

    public void activate() {
        isActive = true;
        collision.isActive = true;
        KeyboardController.addKeyTyped(keyTyped);
    }

    public void deactivate() {
        isActive = false;
        collision.isActive = false;
        KeyboardController.removeKeyTyped(keyTyped);
    }

    public void interact() {

    }

}
