package net.egartley.gamelib.logic.interaction;

import net.egartley.gamelib.abstracts.MapSector;
import org.newdawn.slick.Color;

/**
 * An area (boundary) where a map sector change can occur
 */
public class MapSectorChangeBoundary extends Boundary {

    /**
     * The map sector where the player will be going to after colliding with this boundary
     */
    public MapSector to;

    public MapSectorChangeBoundary(int x, int y, int width, int height, MapSector to) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        top = y;
        bottom = top + height;
        left = x;
        right = left + width;
        drawColor = Color.magenta;
        this.to = to;
    }

}
