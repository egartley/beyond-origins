package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.MapSectorChangeCollision;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeBoundary;

import java.awt.*;
import java.util.ArrayList;

/**
 * Sector of a map
 *
 * @see Map
 */
public abstract class MapSector {

    /**
     * Default tile size in pixels
     */
    private final short TILE_SIZE = 32;
    /**
     * Default width or height of a {@link MapSectorChangeBoundary}
     */
    private final short BOUNDARY_SIZE = 18;

    public static final byte TOP = 0;
    public static final byte RIGHT = 1;
    public static final byte BOTTOM = 2;
    public static final byte LEFT = 3;

    /**
     * Change in x-axis while rendering
     */
    private int deltaX;
    /**
     * Change in y-axis while rendering
     */
    private int deltaY;

    /**
     * The map in which the sector is located
     */
    protected Map parent;
    /**
     * Boundaries, or areas, of all of the possible sector changes
     */
    protected ArrayList<MapSectorChangeBoundary> changeBoundaries;
    /**
     * All of the collisions for the sector changes
     */
    protected ArrayList<MapSectorChangeCollision> changeCollisions;
    /**
     * Entities that only exist within the sector
     *
     * @see Entity#isSectorSpecific
     */
    public ArrayList<Entity> entities;
    /**
     * The sector's definition, such as its tiles and other properties
     */
    protected MapSectorDefinition definition;

    /**
     * Creates a new sector with the given definition
     *
     * @param parent     The map that this sector is in
     * @param definition The {@link MapSectorDefinition} to use
     */
    public MapSector(Map parent, MapSectorDefinition definition) {
        this.parent = parent;
        this.definition = definition;
        changeBoundaries = new ArrayList<>();
        changeCollisions = new ArrayList<>();
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
     * Called when leaving the sector
     */
    public abstract void onPlayerLeave(MapSector goingTo);

    public void setNeighborAt(MapSector neighbor, byte direction) {
        setNeighborAt(neighbor, direction, false);
    }

    private void setNeighborAt(MapSector neighbor, byte direction, boolean didSetInverse) {
        MapSectorChangeBoundary changeBoundary = null;
        switch (direction) {
            case TOP:
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, BOTTOM, true);
                }
                changeBoundary = new MapSectorChangeBoundary(0, 0, Game.WINDOW_WIDTH, BOUNDARY_SIZE, neighbor);
                break;
            case RIGHT:
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, LEFT, true);
                }
                changeBoundary = new MapSectorChangeBoundary(Game.WINDOW_WIDTH - BOUNDARY_SIZE, 0, BOUNDARY_SIZE, Game.WINDOW_HEIGHT, neighbor);
                break;
            case BOTTOM:
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, TOP, true);
                }
                changeBoundary = new MapSectorChangeBoundary(0, Game.WINDOW_HEIGHT - BOUNDARY_SIZE, Game.WINDOW_WIDTH, BOUNDARY_SIZE, neighbor);
                break;
            case LEFT:
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, RIGHT, true);
                }
                changeBoundary = new MapSectorChangeBoundary(0, 0, BOUNDARY_SIZE, Game.WINDOW_HEIGHT, neighbor);
                break;
            default:
                Debug.warning("Unknown direction specified while attempting to set a sector neighbor! (" + direction + ")");
                break;
        }

        if (changeBoundary != null) {
            changeBoundaries.add(changeBoundary);
            changeCollisions.add(new MapSectorChangeCollision(changeBoundary, Entities.PLAYER.boundary, changeBoundary.to, this, parent));
        } else {
            Debug.warning("Could not set a sector neighbor (\"" + neighbor + "\") for \"" + this + "\"!");
        }
    }

    /**
     * Renders all of the sector's tiles, defined by {@link #definition}
     *
     * @param graphics The {@link java.awt.Graphics Graphics} object to use
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

    /**
     * @return <code>parent.toString()</code> + ", sector " +  <code>(parent.sectors.indexOf(this) + 1)</code>
     * @see #parent
     */
    public String toString() {
        return parent.toString() + ", sector " + (parent.sectors.indexOf(this) + 1);
    }

}
