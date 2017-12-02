package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * A "sector" of map that fills the entire window
 * 
 * @author Evan Gartley
 * @see Map
 */
public abstract class MapSector {

	/**
	 * Creates a new map sector with the provided definition
	 * 
	 * @param def
	 *            The {@link MapSectorDefinition} to use
	 */
	public MapSector(MapSectorDefinition def) {
		definition = def;
	}

	/**
	 * This sector's definition, which includes its tiles
	 */
	public MapSectorDefinition definition;

	public abstract void render(Graphics graphics);

	public abstract void tick();

	/**
	 * Called upon entering this sector
	 */
	public abstract void onEnter();

	/**
	 * Called upon leaving this sector
	 */
	public abstract void onExit();

	/**
	 * Renders all of the tiles within {@link #definition}
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object to use
	 * @param ix
	 *            Initial x-axis coordinate
	 * @param iy
	 *            Initial y-axis coordinate
	 * @see MapSectorDefinition
	 */
	public void drawTiles(Graphics graphics, int ix, int iy) {
		int changeY = 0, changeX = 0;
		for (ArrayList<MapTile> row : definition.tiles) {
			for (MapTile tile : row) {
				graphics.drawImage(tile.bufferedImage, ix + changeX, iy + changeY, null);
				changeX += 32;
			}
			changeX = 0;
			changeY += 32;
		}
	}

}