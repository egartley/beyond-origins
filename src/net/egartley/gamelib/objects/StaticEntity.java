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
     * @see Entity#Entity(String, Sprite) Entity(String, Sprite)
     */
    public StaticEntity(String id, Sprite sprite) {
        super(id, sprite);
        isAnimated = false;
        setBoundaries();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x(), y(), null);
    }

}
