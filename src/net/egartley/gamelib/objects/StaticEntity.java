package net.egartley.gamelib.objects;

import net.egartley.gamelib.graphics.Sprite;

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

}
