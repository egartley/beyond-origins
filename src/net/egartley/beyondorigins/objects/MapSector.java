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

	private final short DEFAULT_TILE_SIZE = 32;

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
	public abstract void onPlayerEnter();

	/**
	 * Called upon leaving this sector
	 */
	public abstract void onPlayerDeparture();

	/**
	 * Renders all of the tiles within {@link #definition} starting at 0, 0
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object to use
	 * @see MapSectorDefinition
	 */
	public void drawTiles(Graphics graphics) {
		drawTiles(graphics, 0, 0);
	}

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
				changeX += DEFAULT_TILE_SIZE;
			}
			changeX = 0;
			changeY += DEFAULT_TILE_SIZE;
		}
	}

}
