package net.egartley.beyondorigins.logic.interaction;

import net.egartley.beyondorigins.objects.MapSector;

import java.awt.*;

/**
 * An area (boundary) where a map sector change can occur
 */
public class MapSectorChangeBoundary extends Boundary {

    /**
     * The map sector where the player will be going to after colliding with this
     * boundary
     */
    public MapSector to;

    public MapSectorChangeBoundary(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        top = y;
        bottom = top + height;
        left = x;
        right = left + width;
        drawColor = Color.ORANGE;
    }

    @Override
    public void tick() {

    }

}
