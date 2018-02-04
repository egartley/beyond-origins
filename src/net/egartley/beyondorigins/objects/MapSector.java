package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.MapSectorChangeAreaCollision;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeArea;

/**
 * A "sector" of a map, which fills the entire window
 * 
 * @author Evan Gartley
 * @see Map
 */
public abstract class MapSector {

	private final short TILE_SIZE = 32;
	private int deltaX, deltaY;

	public Map parent;
	public ArrayList<MapSectorChangeArea> changeAreas;
	public ArrayList<MapSectorChangeAreaCollision> changeAreaCollisions;
	/**
	 * This sector's definition, which includes its tiles
	 */
	public MapSectorDefinition definition;

	/**
	 * Creates a new map sector with the provided definition
	 * 
	 * @param parent
	 *            The map that this sector is in
	 * @param def
	 *            The {@link MapSectorDefinition} to use
	 * @param areas
	 *            The areas where a sector change will occur
	 */
	public MapSector(Map parent, MapSectorDefinition def, MapSectorChangeArea... areas) {
		this.parent = parent;
		definition = def;
		changeAreas = new ArrayList<MapSectorChangeArea>();
		changeAreaCollisions = new ArrayList<MapSectorChangeAreaCollision>();
		for (MapSectorChangeArea area : areas) {
			changeAreas.add(area);
			changeAreaCollisions.add(new MapSectorChangeAreaCollision(area, Entities.PLAYER.boundary) {
				public void onCollide() {
					parent.changeSector(area.to);
				}
			});
		}
	}

	public abstract void render(Graphics graphics);

	public abstract void tick();

	/**
	 * Called upon entering this sector
	 */
	public abstract void onPlayerEnter();

	/**
	 * Called upon leaving this sector
	 */
	public abstract void onPlayerLeave();

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
		deltaX = 0;
		deltaY = 0;
		for (ArrayList<MapTile> row : definition.tiles) {
			for (MapTile tile : row) {
				graphics.drawImage(tile.bufferedImage, ix + deltaX, iy + deltaY, null);
				deltaX += TILE_SIZE;
			}
			deltaX = 0;
			deltaY += TILE_SIZE;
		}
	}

}
