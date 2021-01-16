package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.abstracts.StaticEntity;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;

/**
 * Testing for changing maps
 */
public class WarpPad extends StaticEntity {

    public WarpPad(Sprite sprite, int x, int y) {
        super("Warp Portal", sprite);
        setPosition(x, y);
        isSectorSpecific = true;
        isDualRendered = false;
        isTraversable = false;
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(0)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setCollisions() {
    }

    @Override
    protected void setInteractions() {
    }

}
