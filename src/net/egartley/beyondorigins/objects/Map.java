package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Represents a collection of "sectors" for a specific map, or "level"
 * 
 * @author Evan Gartley
 * @see MapSector
 * @see MapSectorDefinition
 * @see MapTile
 */
public abstract class Map {

	/**
	 * Collection of all the possible sectors
	 * 
	 * @see MapSector
	 * @see MapSectorDefinition
	 */
	public ArrayList<MapSector> sectors;
	/**
	 * The current sector to use while rendering
	 * 
	 * @see MapSector
	 * @see MapSectorDefinition
	 */
	public MapSector currentSector;

	public abstract void tick();

	public abstract void render(Graphics graphics);

	/**
	 * Method for changing the current sector
	 * 
	 * @param sector
	 *            The new {@link MapSector} to go to
	 * @see MapSector
	 */
	public abstract void changeSector(MapSector sector);

}