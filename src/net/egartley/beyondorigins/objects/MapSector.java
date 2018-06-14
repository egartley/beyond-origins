package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.MapSectorChangeCollision;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeBoundary;

import java.awt.*;
import java.util.ArrayList;

/**
 * Sector, or portion, of a map, which fills the entire window
 *
 * @see Map
 */
public abstract class MapSector {

    /**
     * Default tile size in pixels
     */
    private final short TILE_SIZE = 32;
    /**
     * Change in x-axis while rendering
     */
    private int deltaX;
    /**
     * Change in y-axis while rendering
     */
    private int deltaY;

    /**
     * The map in which this sector is located
     */
    protected Map parent;
    /**
     * Boundaries, or areas, of all of the sector changes
     */
    public ArrayList<MapSectorChangeBoundary> changeBoundaries;
    /**
     * All of the collision for the sector changes
     */
    protected ArrayList<MapSectorChangeCollision> changeCollisions;
    /**
     * Entities (static or animated) that are specific to this sector
     */
    public ArrayList<Entity> entities;
    /**
     * This sector's definition, which includes its tiles
     */
    protected MapSectorDefinition definition;

    /**
     * Creates a new map sector with the provided definition
     *
     * @param parent     The map that this sector is in
     * @param definition The {@link MapSectorDefinition} to use
     * @param boundaries The areas, or boundaries, where a sector change can occur
     */
    public MapSector(Map parent, MapSectorDefinition definition, MapSectorChangeBoundary... boundaries) {
        this.parent = parent;
        this.definition = definition;
        changeBoundaries = new ArrayList<>();
        changeCollisions = new ArrayList<>();
        MapSector me = this;

        for (MapSectorChangeBoundary changeBoundary : boundaries) {
            changeBoundaries.add(changeBoundary);
            changeCollisions.add(new MapSectorChangeCollision(changeBoundary, Entities.PLAYER.boundary) {
                @Override
                public void onCollide() {
                    parent.changeSector(changeBoundary.to, me);
                }
            });
        }
    }

    /**
     * Minimum requirement for sector rendering
     *
     * @param graphics The {@link java.awt.Graphics Graphics} object to use
     */
    public void render(Graphics graphics) {
        drawTiles(graphics);
    }

    /**
     * Obvious what this does
     */
    public abstract void tick();

    /**
     * Called upon entering this sector
     */
    public abstract void onPlayerEnter(MapSector comingFrom);

    /**
     * Called upon leaving this sector
     */
    public abstract void onPlayerLeave(MapSector goingTo);

    /**
     * Renders all of the tiles within {@link #definition} starting at 0, 0
     *
     * @param graphics The {@link java.awt.Graphics Graphics} object to use
     * @see MapSectorDefinition
     */
    private void drawTiles(Graphics graphics) {
        deltaX = 0;
        deltaY = 0;
        for (ArrayList<MapTile> row : definition.tiles) {
            for (MapTile tile : row) {
                graphics.drawImage(tile.bufferedImage, deltaX, deltaY, null);
                deltaX += TILE_SIZE;
            }
            deltaX = 0;
            deltaY += TILE_SIZE;
        }
    }

    public String toString() {
        return parent + ", sector " + parent.sectors.indexOf(this);
    }

}
