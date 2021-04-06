package net.egartley.beyondorigins.core.logic.interaction;

import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.graphics.Sprite;
import org.newdawn.slick.Color;

/**
 * A {@link Boundary} that is meant for an {@link Entity}
 */
public class EntityBoundary extends Boundary {

    public String name;
    public Entity entity;

    public EntityBoundary(Entity entity) {
        this(entity, entity.sprite);
    }

    public EntityBoundary(Entity entity, Sprite sprite) {
        this(entity, sprite, new BoundaryPadding(0));
    }

    public EntityBoundary(Entity entity, Sprite sprite, BoundaryPadding padding) {
        this(entity, sprite.width, sprite.height, padding);
    }

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
        x = entity.x() - horizontalOffset;
        y = entity.y() - verticalOffset;
        super.tick();
    }

    public String toString() {
        return name;
    }

}
