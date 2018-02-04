package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.logic.events.MapSectorChangeEvent;

/**
 * Represents a collection of "sectors" for a specific map, or "level"
 * 
 * @author Evan Gartley
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

	public Map() {
		sectors = new ArrayList<MapSector>();
	}

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

	/**
	 * Called when the current sector changes ({@link #changeSector(MapSector)})
	 */
	public void onSectorChange(MapSectorChangeEvent event) {

	}

}
