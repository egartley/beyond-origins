package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

/**
 * A collection of map tiles used for defining a map sector
 *
 * @see MapSector
 * @see MapTile
 */
public class MapSectorDefinition {

    /**
     * Collection of map tiles
     *
     * @see MapTile
     */
    public ArrayList<ArrayList<MapTile>> tiles;

    /**
     * Creates a new map sector definition
     *
     * @see MapSector
     * @see MapTile
     */
    public MapSectorDefinition(ArrayList<ArrayList<MapTile>> tileArray) {
        tiles = tileArray;
    }

}
