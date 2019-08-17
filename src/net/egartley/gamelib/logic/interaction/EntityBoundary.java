package net.egartley.gamelib.logic.interaction;

import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.objects.AnimatedEntity;
import net.egartley.gamelib.objects.Entity;

import java.awt.*;

/**
 * Represents a {@link Boundary} that is specifically tailored for use with an {@link Entity}
 */
public class EntityBoundary extends Boundary {

    /**
     * The entity in which to base the boundary
     */
    public Entity parent;
    /**
     * Human-readable identification for this entity boundary
     */
    public String name;

    /**
     * Creates a new boundary for the given entity
     *
     * @param entity
     *         The entity to use
     * @param sprite
     *         Sprite to use for getting width and height
     * @param padding
     *         The padding to apply
     */
    public EntityBoundary(Entity entity, Sprite sprite, BoundaryPadding padding) {
        this(entity, sprite.width, sprite.height, padding);
    }

    /**
     * Creates a new boundary for the given entity
     *
     * @param entity
     *         The entity to use
     * @param width
     *         Width of the boundary (not including the left or right padding)
     * @param height
     *         Height of the boundary (not including the top or bottom padding)
     * @param padding
     *         The padding to apply
     *
     * @see Boundary
     */
    public EntityBoundary(Entity entity, int width, int height, BoundaryPadding padding) {
        this(entity, width, height, padding, new BoundaryOffset(0, 0, 0, 0));
    }

    /**
     * Creates a new boundary for the given entity
     *
     * @param entity
     *         The entity to use
     * @param width
     *         Width of the boundary (not including the left or right padding)
     * @param height
     *         Height of the boundary (not including the top or bottom padding)
     * @param padding
     *         The padding to apply
     * @param offset
     *         The offset to apply
     *
     * @see Boundary
     */
    public EntityBoundary(Entity entity, int width, int height, BoundaryPadding padding, BoundaryOffset offset) {
        parent = entity;
        this.padding = padding;
        this.width = width + padding.left + padding.right;
        this.height = height + padding.top + padding.bottom;
        this.offset = offset;
        horizontalOffset = padding.left + offset.left - offset.right;
        verticalOffset = padding.top + offset.top - offset.bottom;
        name = entity + "/Boundary";
        // call tick so that x and y are actually set
        tick();
        setColor();
    }

    public void setColor() {
        if (!(parent instanceof AnimatedEntity)) {
            drawColor = Color.BLACK;
        } else {
            drawColor = Color.ORANGE; // man bad!
        }
    }

    @Override
    public void tick() {
        x = parent.x() - horizontalOffset;
        y = parent.y() - verticalOffset;
        super.tick();
    }

    public String toString() {
        return name;
    }

}
