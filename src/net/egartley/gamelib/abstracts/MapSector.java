package net.egartley.gamelib.abstracts;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.graphics.MapTile;
import net.egartley.gamelib.interfaces.Tickable;
import net.egartley.gamelib.logic.collision.MapSectorChangeCollision;
import net.egartley.gamelib.logic.interaction.MapSectorChangeBoundary;
import net.egartley.gamelib.objects.MapSectorDefinition;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Specific part, or area, of a map
 *
 * @see Map
 */
public abstract class MapSector implements Tickable {

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
    /**
     * Number of rows of tiles in any sector
     */
    public static final short TILE_ROWS = 30;
    /**
     * Number of columns of tiles in any sector
     */
    public static final short TILE_COLUMNS = 17;

    private int deltaX;
    private int deltaY;

    /**
     * The map this sector is a part of
     */
    protected Map parent;
    /**
     * The sector's neighbors
     */
    private ArrayList<MapSector> neighbors;
    /**
     * Boundaries, or areas, of all of the possible sector changes
     */
    protected ArrayList<MapSectorChangeBoundary> changeBoundaries;
    /**
     * All of the collisions for the sector changes
     */
    private ArrayList<MapSectorChangeCollision> changeCollisions;
    /**
     * Entities that only "exist" within the sector
     *
     * @see Entity#isSectorSpecific
     */
    public ArrayList<Entity> entities;
    /**
     * Entities that are queued for removal from {@link #entities}
     */
    private Entity[] queuedForRemoval;
    /**
     * Entities that are queued for addition to {@link #entities}
     */
    private Entity[] queuedForAddition;
    /**
     * The sector's definition, such as its tiles and other properties
     */
    protected MapSectorDefinition definition;

    public boolean didInitialize;

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
        for (Entity e : entities) {
            if (e.isDualRendered) {
                e.drawFirstLayer(graphics);
            }
        }
        Entities.PLAYER.render(graphics);
        for (Entity e : entities) {
            if (e.isDualRendered) {
                e.drawSecondLayer(graphics);
            } else {
                e.render(graphics);
            }
        }

        if (Game.debug) {
            changeBoundaries.forEach(boundary -> boundary.draw(graphics));
        }
    }

    /**
     * Minimum requirement for each tick, must be called first in any implementation
     */
    @Override
    public void tick() {
        if (queuedForAddition != null && queuedForAddition.length > 0) {
            Collections.addAll(entities, queuedForAddition);
            queuedForAddition = null;
        }
        if (queuedForRemoval != null && queuedForRemoval.length > 0) {
            for (Entity entity : queuedForRemoval) {
                entities.remove(entity);
            }
            queuedForRemoval = null;
        }

        Entities.PLAYER.tick();
        changeCollisions.forEach(MapSectorChangeCollision::tick);
        entities.forEach(Entity::tick);
    }

    public void removeEntity(Entity e) {
        removeEntities(new Entity[]{e});
    }

    public void removeEntities(Entity[] e) {
        queuedForRemoval = e;
    }

    public void addEntity(Entity e) {
        addEntities(new Entity[]{e});
    }

    public void addEntities(Entity[] e) {
        queuedForAddition = e;
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
                Entities.PLAYER.y(PLAYER_ENTRANCE_OFFSET);
                break;
            case LEFT:
                Entities.PLAYER.x(PLAYER_ENTRANCE_OFFSET);
                break;
            case BOTTOM:
                Entities.PLAYER.y(Game.WINDOW_HEIGHT - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.height);
                break;
            case RIGHT:
                Entities.PLAYER.x(Game.WINDOW_WIDTH - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.width);
                break;
            default:
                break;
        }
    }

    /**
     * Updates the player's position in accordance with the sector they just came from
     */
    protected void updatePlayerPosition(MapSector from) {
        playerEnteredFrom(neighbors.indexOf(from));
    }

    /**
     * Renders all of the sector's tiles, defined by {@link #definition}
     */
    protected void drawTiles(Graphics graphics) {
        deltaX = 0;
        deltaY = 0;
        for (int r = 0; r < definition.tiles.size(); r++) {
            ArrayList<MapTile> row = definition.tiles.get(r);
            for (MapTile tile : row) {
                graphics.drawImage(tile.image, deltaX, deltaY, null);
                deltaX += TILE_SIZE;
            }
            deltaX = 0;
            deltaY += TILE_SIZE;
        }
    }

    /**
     * @return parent, sector index
     * @see #parent
     */
    public String toString() {
        return parent.toString() + ", sector " + (parent.sectors.indexOf(this) + 1);
    }

}
