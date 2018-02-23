package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.MapSectorChangeCollision;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeBoundary;

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
	public ArrayList<MapSectorChangeBoundary> changeBoundaries;
	public ArrayList<MapSectorChangeCollision> changeCollisions;
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
	 * @param boundaries
	 *            The areas, or boundaries, where a sector change can occur
	 */
	public MapSector(Map parent, MapSectorDefinition def, MapSectorChangeBoundary... boundaries) {
		this.parent = parent;
		definition = def;
		changeBoundaries = new ArrayList<MapSectorChangeBoundary>();
		changeCollisions = new ArrayList<MapSectorChangeCollision>();
		
		for (MapSectorChangeBoundary changeBoundary : boundaries) {
			// initialize each collision
			changeBoundaries.add(changeBoundary);
			changeCollisions.add(new MapSectorChangeCollision(changeBoundary, Entities.PLAYER.boundary) {
				@Override
				public void onCollide() {
					parent.changeSector(changeBoundary.goingTo);
				}
			});
		}
	}

	public abstract void render(Graphics graphics);

	public abstract void tick();

	/**
	 * Called upon entering this sector
	 */
	public abstract void onPlayerEnter(MapSector from);

	/**
	 * Called upon leaving this sector
	 */
	public abstract void onPlayerLeave(MapSector to);

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
