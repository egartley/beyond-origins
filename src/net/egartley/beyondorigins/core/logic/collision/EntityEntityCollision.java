package net.egartley.beyondorigins.core.logic.collision;

import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import org.newdawn.slick.Color;

import java.awt.*;
import java.util.ArrayList;

/**
 * A collision between two different entities (specifically their specified boundaries)
 */
public class EntityEntityCollision {

    private boolean firedEvent;
    private final Rectangle[] rectangles;

    public boolean isCollided;
    public boolean isActive = true;
    public boolean isMovementRestricting;
    public Entity[] entities;
    public EntityBoundary[] boundaries;
    public EntityEntityCollisionEvent lastEvent;

    public EntityEntityCollision(EntityBoundary boundary1, EntityBoundary boundary2) {
        boundaries = new EntityBoundary[]{boundary1, boundary2};
        rectangles = new Rectangle[]{boundaries[0].asRectangle(), boundaries[1].asRectangle()};
        rectangles[0].width += 2;
        rectangles[1].width += 2;
        rectangles[0].height += 2;
        rectangles[1].height += 2;
        entities = new Entity[]{boundaries[0].entity, boundaries[1].entity};
    }

    public void tick() {
        if (!isActive || !entities[0].canCollide || !entities[1].canCollide) {
            return;
        }
        rectangles[0].x = boundaries[0].x - 1;
        rectangles[1].x = boundaries[1].x - 1;
        rectangles[0].y = boundaries[0].y - 1;
        rectangles[1].y = boundaries[1].y - 1;
        // the magic happens right here!!
        isCollided = rectangles[0].intersects(rectangles[1]);
        if (isCollided && !firedEvent) {
            lastEvent = new EntityEntityCollisionEvent(this);
            onCollide();
            start(lastEvent);
            firedEvent = true;
        }
        if (!isCollided && firedEvent) {
            onCollisionEnd();
            end(lastEvent);
            firedEvent = false;
        }
    }

    /**
     * This is called <em>once</em> after the collision occurs
     */
    public void start(EntityEntityCollisionEvent event) {

    }

    /**
     * This is called <em>once</em> after the collision ends
     */
    public void end(EntityEntityCollisionEvent event) {

    }

    /**
     * End the collision, regardless of whether or not there is still
     * an actual collision between the entities
     */
    public void end() {
        isCollided = false;
        onCollisionEnd();
        end(lastEvent);
        firedEvent = false;
    }

    /**
     * Sets or updates the colors for both boundaries
     */
    private void setBoundaryColors() {
        determineBoundaryColors(entities[0].boundaries);
        determineBoundaryColors(entities[1].boundaries);
    }

    private void determineBoundaryColors(ArrayList<EntityBoundary> boundaries) {
        boundaries.forEach(this::determineBoundaryColor);
    }

    private void determineBoundaryColor(EntityBoundary boundary) {
        boundary.drawColor = boundary.isCollided ? Color.red : Color.orange;
    }

    /**
     * Called right before {@link #start(EntityEntityCollisionEvent)}
     */
    private void onCollide() {
        for (Entity e : entities) {
            e.lastCollision = this;
            e.isCollided = true;
        }
        for (EntityBoundary b : boundaries) {
            b.isCollided = true;
        }
        setBoundaryColors();
    }

    /**
     * Called right before {@link #end(EntityEntityCollisionEvent)}
     */
    private void onCollisionEnd() {
        for (EntityBoundary boundary : boundaries) {
            ArrayList<EntityEntityCollision> concurrent = Collisions.concurrent(boundary.entity);
            if (concurrent.size() == 0) {
                boundary.isCollided = false;
                continue;
            }
            for (EntityEntityCollision collision : concurrent) {
                if (collision.isCollided) {
                    // one of the entities has another collided collision (not this)
                    // check to see if either of the collided collision's boundaries are of this boundaries
                    boundary.isCollided = collision.boundaries[0] == boundary || collision.boundaries[1] == boundary;
                    // break because boundary has at least one other collided collision, move on to other one
                    if (boundary.isCollided) {
                        break;
                    }
                }
                boundary.isCollided = false;
            }
        }
        for (Entity entity : entities) {
            for (EntityBoundary boundary : entity.boundaries) {
                if (boundary.isCollided) {
                    // at least one boundary is collided
                    entity.isCollided = true;
                    break;
                }
                entity.isCollided = false;
            }
        }
        setBoundaryColors();
    }

    @Override
    public String toString() {
        return "\"" + boundaries[0] + "\" into \"" + boundaries[1] + "\"";
    }

}
