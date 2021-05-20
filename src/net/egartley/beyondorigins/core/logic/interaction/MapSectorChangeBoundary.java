package net.egartley.beyondorigins.core.logic.interaction;

import net.egartley.beyondorigins.core.abstracts.MapSector;
import org.newdawn.slick.Color;

/**
 * A boundary specifically for map sector changes
 */
public class MapSectorChangeBoundary extends Boundary {

    public MapSector to;

    public MapSectorChangeBoundary(int x, int y, int width, int height, MapSector to) {
        this.x = x;
        this.y = y;
        this.to = to;
        this.width = width;
        this.height = height;
        top = y;
        bottom = top + height;
        left = x;
        right = left + width;
        drawColor = Color.magenta;
    }

}
