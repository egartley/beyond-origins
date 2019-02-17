package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.MapSectorChangeCollision;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeBoundary;

import java.awt.*;
import java.util.ArrayList;

/**
 * Specific part, or area, of a map
 *
 * @see Map
 */
public abstract class MapSector {

    /**
     * Maximum number of neighbors one sector can have
     */
    private final byte MAX_NEIGHBORS = 4;
    /**
     * Tile width and height in pixels
     */
    private final short TILE_SIZE = 32;
    /**
     * Default width/height of a {@link MapSectorChangeBoundary}
     */
    private final short BOUNDARY_SIZE = 18;
    /**
     * How much to offset one of the player's coordinates when entering from another sector, if applicable
     */
    private final short PLAYER_ENTRANCE_OFFSET = BOUNDARY_SIZE + 4;

    public static final byte TOP = 0;
    public static final byte RIGHT = 1;
    public static final byte BOTTOM = 2;
    public static final byte LEFT = 3;

    private int deltaX;
    private int deltaY;

    /**
     * The sector's map
     */
    protected Map parent;
    /**
     * The sector's neighbors
     */
    private ArrayList<MapSector> neighbors;
    /**
     * Boundaries, or areas, of all of the possible sector changes
     */
    private ArrayList<MapSectorChangeBoundary> changeBoundaries;
    /**
     * All of the collisions for the sector changes
     */
    private ArrayList<MapSectorChangeCollision> changeCollisions;
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
        neighbors = new ArrayList<>(MAX_NEIGHBORS);
        for (byte i = 0; i < MAX_NEIGHBORS; i++) {
            neighbors.add(null);
        }
        entities = new ArrayList<>();
    }

    /**
     * Called upon entering this sector
     */
    public abstract void onPlayerEnter(MapSector from);

    /**
     * Called when leaving the sector
     */
    public abstract void onPlayerLeave(MapSector to);

    /**
     * Initialize things such as sector-specific entities
     */
    public abstract void initialize();

    void onPlayerEnter_internal() {
        Util.fixCrossSectorCollisions(entities);
    }

    void onPlayerLeave_internal() {
        Util.fixCrossSectorCollisions(entities);
    }

    /**
     * Minimum requirement for rendering, must be called first in any implementation
     */
    public void render(Graphics graphics) {
        drawTiles(graphics);
        for (MapSectorChangeBoundary boundary : changeBoundaries) {
            boundary.draw(graphics);
        }
    }

    /**
     * Minimum requirement for each tick, must be called first in any implementation
     */
    public void tick() {
        Entities.PLAYER.tick();
        for (MapSectorChangeCollision collision : changeCollisions) {
            collision.tick();
        }
    }

    /**
     * Sets a neighboring sector in the direction, as well as reciprocating it
     *
     * @param neighbor  The sector to set as a neighbor
     * @param direction {@link #TOP}, {@link #LEFT}, {@link #BOTTOM}, or {@link #RIGHT}
     */
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
            Debug.warning("Could not set a neighbor (\"" + neighbor + "\") for \"" + this + "\"!");
        }

        neighbors.set(direction, neighbor);
    }

    private void playerEnteredFrom(int direction) {
        switch (direction) {
            case TOP:
                Entities.PLAYER.y = PLAYER_ENTRANCE_OFFSET;
                break;
            case LEFT:
                Entities.PLAYER.x = PLAYER_ENTRANCE_OFFSET;
                break;
            case BOTTOM:
                Entities.PLAYER.y = Game.WINDOW_HEIGHT - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.height;
                break;
            case RIGHT:
                Entities.PLAYER.x = Game.WINDOW_WIDTH - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.width;
                break;
            default:
                break;
        }
    }

    /**
     * Updates the player's position in accordance with the sector it just came from
     */
    protected void updatePlayerPosition(MapSector from) {
        playerEnteredFrom(neighbors.indexOf(from));
    }

    /**
     * Renders all of the sector's tiles, defined by {@link #definition}
     */
    private void drawTiles(Graphics graphics) {
        deltaX = 0;
        deltaY = 0;
        for (int r = 0; r < definition.tiles.size(); r++) {
            ArrayList<MapTile> row = definition.tiles.get(r);
            for (int c = 0; c < row.size(); c++) {
                MapTile tile = row.get(c);
                graphics.drawImage(tile.bufferedImage, deltaX, deltaY, null);
                /* if (Game.debug) {
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(deltaX, deltaY, TILE_SIZE, TILE_SIZE);
                    graphics.setFont(new Font(graphics.getFont().getFontName(), Font.PLAIN, 8));
                    graphics.setColor(Color.WHITE);
                    graphics.drawString(r + ", " + c, deltaX + 2, deltaY + 8);
                } */
                deltaX += TILE_SIZE;
            }
            deltaX = 0;
            deltaY += TILE_SIZE;
        }
    }

    /**
     * @return <code>parent.toString()</code> + ", sector " + <code>(parent.sectors.indexOf(this) + 1)</code>
     * @see #parent
     */
    public String toString() {
        return parent.toString() + ", sector " + (parent.sectors.indexOf(this) + 1);
    }

}
