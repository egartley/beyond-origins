package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.engine.entities.VisibleEntity;
import net.egartley.beyondorigins.engine.graphics.Sprite;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;

/**
 * Test object for switching between maps
 */
public class WarpPad extends VisibleEntity {

    public WarpPad(Sprite sprite, int x, int y) {
        super("Warp Portal", sprite);
        setPosition(x, y);
        isSectorSpecific = true;
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

}
