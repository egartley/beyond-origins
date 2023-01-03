package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.engine.entities.VisibleEntity;
import net.egartley.beyondorigins.engine.graphics.Sprite;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.BoundaryPadding;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;

/**
 * Rock that the player cannot walk over, but can walk behind
 */
public class DefaultRock extends VisibleEntity {

    public DefaultRock(int x, int y) {
        super("Rock", Entities.getSpriteTemplate(Entities.TEMPLATE_ROCK));
        setPosition(x, y);
        isSectorSpecific = true;
        isTraversable = false;
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, image.getWidth(), image.getHeight(),
                new BoundaryPadding(-4, 1, -8, 1)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setCollisions() {

    }

}
