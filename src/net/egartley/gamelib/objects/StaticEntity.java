package net.egartley.gamelib.objects;

import net.egartley.gamelib.graphics.Sprite;

import java.awt.*;

/**
 * An entity that does have not any animations
 *
 * @see Entity
 */
public abstract class StaticEntity extends Entity {

    /**
     * Creates a new static entity, while setting {@link Entity#isAnimated} to <code>false</code>
     *
     * @see Entity#Entity(String) Entity(String)
     */
    public StaticEntity(String id) {
        super(id);
        isAnimated = false;
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
        graphics.drawImage(image, x(), y(), null);
    }

}
