package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.abstracts.VisibleEntity;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;

/**
 * Testing for changing maps
 */
public class WarpPad extends VisibleEntity {

    public WarpPad(Sprite sprite, int x, int y) {
        super("Warp Portal", sprite);
        setPosition(x, y);
        isSectorSpecific = true;
        isDualRendered = false;
        isTraversable = false;
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setCollisions() {
    }

    @Override
    protected void setInteractions() {
    }

}
