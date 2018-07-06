package net.egartley.beyondorigins.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An entity that cannot be animated
 *
 * @see Entity
 */
public abstract class StaticEntity extends Entity {

    /**
     * The entity's frame that is used while rendering
     */
    protected BufferedImage image;

    /**
     * Creates a new static entity, while setting {@link Entity#isAnimated} to false and {@link Entity#isStatic} to
     * true
     *
     * @param id
     *         Human-readable ID for the entity
     *
     * @see Entity
     */
    private StaticEntity(String id) {
        super(id);
        isAnimated = false;
        isStatic = true;
    }

    /**
     * Creates a new static entity, while setting {@link Entity#isAnimated} to false and {@link Entity#isStatic} to
     * true
     *
     * @param id
     *         Human-readable ID for the entity
     * @param sprite
     *         The sprite to use
     *
     * @see Entity
     */
    public StaticEntity(String id, Sprite sprite) {
        this(id);
        this.sprite = sprite;
        image = sprite.asBufferedImage(0);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, (int) x, (int) y, null);
        drawDebug(graphics);
    }

    @Override
    public void tick() {
        super.tick();
    }

}
