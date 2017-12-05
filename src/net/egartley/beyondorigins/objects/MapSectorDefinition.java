package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

/**
 * A collection of map tiles used for defining a map sector
 * 
 * @author Evan Gartley
 * @see MapSector
 * @see MapTile
 */
public class MapSectorDefinition {

	/**
	 * Creates a new map sector definition
	 * 
	 * @param tileArray
	 *            Collection of map tiles
	 * @see MapSector
	 * @see MapTile
	 */
	public MapSectorDefinition(ArrayList<ArrayList<MapTile>> tileArray) {
		tiles = tileArray;
	}

	/**
	 * Collection of map tiles
	 * 
	 * @see MapTile
	 */
	public ArrayList<ArrayList<MapTile>> tiles;

}
