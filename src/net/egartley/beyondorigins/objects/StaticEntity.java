package net.egartley.beyondorigins.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An entity that does have not any animations
 *
 * @see Entity
 */
public abstract class StaticEntity extends Entity {

    /**
     * The buffered image to use when rendering
     */
    protected BufferedImage image;

    /**
     * Creates a new static entity, while setting {@link Entity#isAnimated} to <code>false</code> and {@link
     * Entity#isStatic} to <code>true</code>
     *
     * @see Entity#Entity(String) Entity(String)
     */
    private StaticEntity(String id) {
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
