package net.egartley.beyondorigins.objects;

import java.awt.*;

/**
 * An entity that does have not any animations
 *
 * @see Entity
 */
public abstract class StaticEntity extends Entity {

    /**
     * Creates a new static entity, while setting {@link Entity#isAnimated} to <code>false</code> and {@link
     * Entity#isStatic} to <code>true</code>
     *
     * @see Entity#Entity(String) Entity(String)
     */
    public StaticEntity(String id) {
        super(id);
        isAnimated = false;
        isStatic = true;
    }

    /**
     * Calls {@link #StaticEntity(String)}, sets {@link #sprite}, then sets {@link #image} to
     * {@link Sprite#toBufferedImage()}
     */
    public StaticEntity(String id, Sprite sprite) {
        this(id);
        this.sprite = sprite;
        image = sprite.toBufferedImage();
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
