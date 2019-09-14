package net.egartley.gamelib.logic.interaction;

import net.egartley.beyondorigins.controllers.KeyboardController;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.input.KeyTyped;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;

import java.awt.event.KeyEvent;

public class EntityEntityInteraction {

    public Entity[] entities;
    public EntityEntityCollision collision;

    private static int defaultKeyCode = KeyEvent.VK_ENTER;

    private boolean isActive;
    private boolean didInteract;
    private KeyTyped keyTyped;

    public EntityEntityInteraction(Entity e1, Entity e2) {
        this(e1.defaultBoundary, e2.defaultBoundary);
    }

    public EntityEntityInteraction(EntityBoundary b1, EntityBoundary b2) {
        entities = new Entity[]{b1.parent, b2.parent};
        collision = new EntityEntityCollision(b1, b2) {
            @Override
            public void end(EntityEntityCollisionEvent event) {
                didInteract = false;
            }
        };
        keyTyped = new KeyTyped(defaultKeyCode) {
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
        collision.activate();
        KeyboardController.addKeyTyped(keyTyped);
    }

    public void deactivate() {
        isActive = false;
        collision.deactivate();
        KeyboardController.removeKeyTyped(keyTyped);
    }

    public void interact() {

    }

}
