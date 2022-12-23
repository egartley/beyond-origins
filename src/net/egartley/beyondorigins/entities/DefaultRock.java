package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.abstracts.VisibleEntity;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;

/**
 * Rock that the player cannot walk over, but can walk behind
 */
public class DefaultRock extends VisibleEntity {

    public DefaultRock(Sprite sprite, int x, int y) {
        super("Rock", sprite);
        setPosition(x, y);
        isSectorSpecific = true;
        isDualRendered = true;
        isTraversable = false;
        // top half
        firstLayer = image.getSubImage(0, image.getHeight() / 2, image.getWidth(), image.getHeight() / 2);
        // bottom half
        secondLayer = image.getSubImage(0, 0, image.getWidth(), image.getHeight() / 2);
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
