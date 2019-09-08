package net.egartley.beyondorigins.entities;

import net.egartley.gamelib.abstracts.StaticEntity;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;

/**
 * Rock that the player cannot walk over, but can walk behind
 */
public class DefaultRock extends StaticEntity {

    public DefaultRock(Sprite sprite, int x, int y) {
        super("Rock", sprite);
        setPosition(x, y);

        isSectorSpecific = true;
        isDualRendered = true;
        isTraversable = false;
        // top half
        firstLayer = image.getSubimage(0, image.getHeight() / 2, image.getWidth(), image.getHeight() / 2);
        // bottom half
        secondLayer = image.getSubimage(0, 0, image.getWidth(), image.getHeight() / 2);
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

    @Override
    protected void setInteractions() {

    }

}
