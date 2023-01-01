package net.egartley.beyondorigins.engine.logic.collision;

import net.egartley.beyondorigins.engine.entities.Entity;
import net.egartley.beyondorigins.engine.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import org.newdawn.slick.Color;

import java.awt.*;
import java.util.ArrayList;

/**
 * A collision between two different entities
 */
public class EntityEntityCollision {

    private boolean didFireEvent;
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
        if (!isActive || !entities[0].isAbleToCollide || !entities[1].isAbleToCollide) {
            return;
        }
        rectangles[0].x = boundaries[0].x - 1;
        rectangles[1].x = boundaries[1].x - 1;
        rectangles[0].y = boundaries[0].y - 1;
        rectangles[1].y = boundaries[1].y - 1;
        // the magic happens right here!!
        isCollided = rectangles[0].intersects(rectangles[1]);
        if (isCollided && !didFireEvent) {
            lastEvent = new EntityEntityCollisionEvent(this);
            onCollision();
            start(lastEvent);
            didFireEvent = true;
        }
        if (!isCollided && didFireEvent) {
            onCollisionEnd();
            end(lastEvent);
            didFireEvent = false;
        }
    }

    public void start(EntityEntityCollisionEvent event) {

    }

    /**
     * End the collision, regardless of whether there is still
     * an actual collision between the entities
     */
    public void end() {
        isCollided = false;
        onCollisionEnd();
        end(lastEvent);
        didFireEvent = false;
    }

    public void end(EntityEntityCollisionEvent event) {

    }

    private void determineBoundaryColor(EntityBoundary boundary) {
        boundary.drawColor = boundary.isCollided ? Color.red : Color.orange;
    }

    private void determineBoundaryColors(ArrayList<EntityBoundary> boundaries) {
        boundaries.forEach(this::determineBoundaryColor);
    }

    private void onCollision() {
        for (Entity e : entities) {
            e.lastCollision = this;
            e.isCollided = true;
        }
        for (EntityBoundary b : boundaries) {
            b.isCollided = true;
        }
        setBoundaryColors();
    }

    private void onCollisionEnd() {
        for (EntityBoundary boundary : boundaries) {
            ArrayList<EntityEntityCollision> concurrent = Collisions.getConcurrentWith(boundary.entity);
            if (concurrent.size() == 0) {
                boundary.isCollided = false;
                continue;
            }
            for (EntityEntityCollision c : concurrent) {
                if (c.isCollided) {
                    // one of the entities has another collided collision (not this)
                    // check to see if either of the collided collision's boundaries are of this boundaries
                    boundary.isCollided = c.boundaries[0] == boundary || c.boundaries[1] == boundary;
                    // break because boundary has at least one other collided collision, move on to other one
                    if (boundary.isCollided) {
                        break;
                    }
                }
                boundary.isCollided = false;
            }
        }
        for (Entity e : entities) {
            for (EntityBoundary b : e.boundaries) {
                if (b.isCollided) {
                    e.isCollided = true;
                    break;
                }
                e.isCollided = false;
            }
        }
        setBoundaryColors();
    }

    private void setBoundaryColors() {
        determineBoundaryColors(entities[0].boundaries);
        determineBoundaryColors(entities[1].boundaries);
    }

    @Override
    public String toString() {
        return "\"" + boundaries[0] + "\" into \"" + boundaries[1] + "\"";
    }

}
