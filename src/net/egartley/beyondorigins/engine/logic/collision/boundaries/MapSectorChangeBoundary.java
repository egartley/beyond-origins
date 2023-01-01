package net.egartley.beyondorigins.engine.logic.collision.boundaries;

import net.egartley.beyondorigins.engine.map.MapSector;
import org.newdawn.slick.Color;

/**
 * A boundary specifically for map sector changes
 */
public class MapSectorChangeBoundary extends Boundary {

    public MapSector goingTo;

    public MapSectorChangeBoundary(int x, int y, int width, int height, MapSector goingTo) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.goingTo = goingTo;
        top = y;
        bottom = top + height;
        left = x;
        right = left + width;
        drawColor = Color.magenta;
    }

}
