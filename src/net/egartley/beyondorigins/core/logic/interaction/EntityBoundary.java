package net.egartley.beyondorigins.core.logic.interaction;

import net.egartley.beyondorigins.core.abstracts.Entity;
import org.newdawn.slick.Color;

/**
 * A boundary specifically for entities
 */
public class EntityBoundary extends Boundary {

    public String name;
    public Entity entity;

    /**
     * Creates a new entity boundary using the entity's width and height, with no padding or offset
     */
    public EntityBoundary(Entity entity) {
        this(entity, entity.width, entity.height);
    }

    /**
     * Creates a new entity boundary with no padding or offset
     */
    public EntityBoundary(Entity entity, int width, int height) {
        this(entity, width, height, new BoundaryPadding(0));
    }

    /**
     * Creates a new entity boundary with no offset
     */
    public EntityBoundary(Entity entity, int width, int height, BoundaryPadding padding) {
        this(entity, width, height, padding, new BoundaryOffset(0, 0, 0, 0));
    }

    public EntityBoundary(Entity entity, int width, int height, BoundaryPadding padding, BoundaryOffset offset) {
        this.entity = entity;
        this.padding = padding;
        this.width = width + padding.left + padding.right;
        this.height = height + padding.top + padding.bottom;
        this.offset = offset;
        drawColor = Color.orange;
        name = entity + "/Boundary";
        verticalOffset = padding.top + offset.top - offset.bottom;
        horizontalOffset = padding.left + offset.left - offset.right;
        // call tick so that x and y are actually set
        tick();
    }

    @Override
    public void tick() {
        x = entity.x - horizontalOffset;
        y = entity.y - verticalOffset;
        super.tick();
    }

    public String toString() {
        return name;
    }

}
