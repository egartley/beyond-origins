package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.engine.entities.VisibleEntity;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.BoundaryPadding;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;

/**
 * Basic tree that the player can walk under, but not over
 */
public class DefaultTree extends VisibleEntity {

    public DefaultTree(int x, int y) {
        super("Tree", Entities.getSpriteTemplate(Entities.TEMPLATE_TREE));
        setPosition(x, y);
        isSectorSpecific = true;
        isTraversable = false;
        isRenderPlayerBased = true;
        renderLayerThresholdY = 38;
    }

    @Override
    protected void setBoundaries() {
        defaultBoundary = new EntityBoundary(this, image.getWidth(), image.getHeight(),
                new BoundaryPadding(-24, -24, -24, -24));
        boundaries.add(defaultBoundary);
    }

    @Override
    public void setCollisions() {

    }

}
