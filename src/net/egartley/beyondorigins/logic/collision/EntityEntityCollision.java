package net.egartley.beyondorigins.logic.collision;

import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Entity;

import java.awt.*;
import java.util.ArrayList;

/**
 * A collision between two different entities
 */
public class EntityEntityCollision {

    /**
     * Prevents boundary colors from being set more than they have to
     */
    private boolean previouslyCollided;
    /**
     * Whether or not the onCollide event has been fired
     */
    private boolean firedEvent;

    /**
     * Whether or not the two entities are collided
     */
    public boolean isCollided;

    private Rectangle[] rectangles;

    /**
     * The collision's most recent event
     */
    public EntityEntityCollisionEvent lastEvent;
    public EntityBoundary[] boundaries;

    /**
     * Entities that are to collide
     */
    public Entity[] entities;

    /**
     * Creates a new collision between two entity boundaries
     *
     * @param boundary1
     *         First entity's boundary
     * @param boundary2
     *         Second entity's boundary
     */
    protected EntityEntityCollision(EntityBoundary boundary1, EntityBoundary boundary2) {
        boundaries = new EntityBoundary[]{
                boundary1,
                boundary2
        };
        rectangles = new Rectangle[]{
                boundaries[0].asRectangle(),
                boundaries[1].asRectangle()
        };
        rectangles[0].width += 2;
        rectangles[1].width += 2;
        rectangles[0].height += 2;
        rectangles[1].height += 2;
        entities = new Entity[]{
                boundaries[0].parent,
                boundaries[1].parent
        };
    }

    /**
     * Updates coordinates, then checks for collision between the entities
     *
     * @see #onCollide(EntityEntityCollisionEvent)
     * @see #onCollisionEnd(EntityEntityCollisionEvent)
     */
    public void tick() {
        rectangles[0].x = boundaries[0].x - 1;
        rectangles[1].x = boundaries[1].x - 1;
        rectangles[0].y = boundaries[0].y - 1;
        rectangles[1].y = boundaries[1].y - 1;

        isCollided = rectangles[0].intersects(rectangles[1]);

        if (isCollided && !firedEvent) {
            lastEvent = new EntityEntityCollisionEvent(this);
            onCollide_internal();
            onCollide(lastEvent);
            firedEvent = true;
        }
        if (!isCollided && firedEvent) {
            onCollisionEnd_internal();
            onCollisionEnd(lastEvent);
            firedEvent = false;
        }
        if (previouslyCollided != isCollided) {
            setBoundaryColors();
        }

        previouslyCollided = isCollided;
    }

    /**
     * This is called <em>once</em> after the collision occurs
     *
     * @param event
     *         The collision's event
     */
    public void onCollide(EntityEntityCollisionEvent event) {

    }

    /**
     * This is called <em>once</em> after the collision ends
     *
     * @param event
     *         The collision's event
     */
    public void onCollisionEnd(EntityEntityCollisionEvent event) {

    }

    public void end() {
        isCollided = false;
        onCollisionEnd_internal();
        onCollisionEnd(lastEvent);
        firedEvent = false;
    }

    /**
     * Sets or updates the colors for both boundaries
     */
    private void setBoundaryColors() {
        determineBoundaryColors(entities[0].boundaries);
        determineBoundaryColors(entities[1].boundaries);
    }

    private void determineBoundaryColors(ArrayList<EntityBoundary> e) {
        // fancy :O
        e.forEach(this::determineBoundaryColor);
    }

    private void determineBoundaryColor(EntityBoundary e) {
        if (!e.isCollided) {
            if (e.parent.isStatic) {
                e.drawColor = Color.BLACK;
            } else {
                e.drawColor = Color.ORANGE;
            }
        } else {
            if (e.parent.isStatic) {
                e.drawColor = Color.YELLOW;
            } else {
                e.drawColor = Color.RED;
            }
        }
    }

    /**
     * Called right before {@link #onCollide(EntityEntityCollisionEvent)}
     */
    private void onCollide_internal() {
        for (Entity e : entities) {
            // both entities are collided
            e.lastCollision = this;
            e.concurrentCollisions.add(this);
            e.isCollided = true;
        }
        for (EntityBoundary b : boundaries) {
            // both boundaries are collided
            b.isCollided = true;
        }
    }

    /**
     * Called right before {@link #onCollisionEnd(EntityEntityCollisionEvent)}
     */
    private void onCollisionEnd_internal() {
        // determine boundary.isCollided (before entity.isCollided)
        for (EntityBoundary boundary : boundaries) {
            for (EntityEntityCollision parentCollision : boundary.parent.concurrentCollisions) {
                if (parentCollision.isCollided) {
                    boundary.isCollided = parentCollision.boundaries[0] == boundary || parentCollision
                            .boundaries[1] == boundary;
                    break;
                } else {
                    boundary.isCollided = false;
                }
            }
        }

        // now determine entity.isCollided
        for (Entity entity : entities) {
            entity.concurrentCollisions.remove(this);
            for (EntityBoundary boundary : entity.boundaries) {
                if (boundary.isCollided) {
                    // at least one boundary is collided, therefore the entity should be considered collided
                    entity.isCollided = true;
                    break;
                }
                // no boundaries were collided
                entity.isCollided = false;
            }
        }
    }

    @Override
    public String toString() {
        return boundaries[0] + "/" + boundaries[1];
    }

}