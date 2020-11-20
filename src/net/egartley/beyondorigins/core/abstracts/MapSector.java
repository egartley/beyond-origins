package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.graphics.MapTile;
import net.egartley.beyondorigins.core.interfaces.Damageable;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.collision.MapSectorChangeCollision;
import net.egartley.beyondorigins.core.logic.interaction.MapSectorChangeBoundary;
import net.egartley.beyondorigins.core.ui.NotificationBanner;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

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
    public static final short TILE_ROWS = 17;
    /**
     * Number of columns of tiles in any sector
     */
    public static final short TILE_COLUMNS = 30;

    public int number;

    private int deltaX;
    private int deltaY;

    /**
     * The map this sector is a part of
     */
    protected Map parent;
    /**
     * The sector's neighbors
     */
    private final ArrayList<MapSector> neighbors = new ArrayList<>(MAX_NEIGHBORS);
    /**
     * Boundaries, or areas, of all of the possible sector changes
     */
    protected ArrayList<MapSectorChangeBoundary> changeBoundaries = new ArrayList<>();
    /**
     * All of the collisions for the sector changes
     */
    private final ArrayList<MapSectorChangeCollision> changeCollisions = new ArrayList<>();
    /**
     * Entities that only "exist" within the sector
     *
     * @see Entity#isSectorSpecific
     */
    public ArrayList<Entity> entities = new ArrayList<>();
    public ArrayList<Entity> primaryEntities = new ArrayList<>();
    public ArrayList<Renderable> renderables = new ArrayList<>();
    public ArrayList<Tickable> tickables = new ArrayList<>();

    protected ArrayList<ArrayList<MapTile>> tiles = new ArrayList<>();

    /**
     * Creates a new map sector
     */
    public MapSector(Map parent, int sector) {
        this.parent = parent;
        number = sector;
        for (byte i = 0; i < MAX_NEIGHBORS; i++) {
            neighbors.add(null);
        }
        buildTiles();
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

    public void onPlayerEnter_internal() {
        addEntity(Entities.PLAYER, true);
        Util.fixCrossSectorCollisions(entities);
        initialize();
    }

    public void onPlayerLeave_internal() {
        Util.fixCrossSectorCollisions(entities);
        ArrayList<Entity> e = (ArrayList<Entity>) entities.clone();
        for (Entity entity : e) {
            removeEntity(entity);
        }
        e = (ArrayList<Entity>) primaryEntities.clone();
        for (Entity entity : e) {
            removeEntity(entity, true);
        }
    }

    /**
     * Minimum requirement for rendering, must be called first in any implementation
     */
    public void render(Graphics graphics) {
        drawTiles(graphics);
        try {
            for (Entity e : entities) {
                if (e.isDualRendered) {
                    e.drawFirstLayer(graphics);
                }
            }
            primaryEntities.forEach(r -> r.render(graphics));
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

            renderables.forEach(r -> r.render(graphics));
        } catch (ConcurrentModificationException cme) {
            // Debug.warning("Concurrent modification! (" + cme.getMessage() + ")");
            cme.printStackTrace();
        } catch (Exception e) {
            Debug.error(e);
        }
    }

    /**
     * Minimum requirement for each tick, must be called first in any implementation
     */
    @Override
    public void tick() {
        try {
            tickables.forEach(Tickable::tick);
            changeCollisions.forEach(MapSectorChangeCollision::tick);
        } catch (ConcurrentModificationException cme) {
            // ignore for now
        } catch (Exception e) {
            Debug.error(e);
        }
    }

    public void addEntity(Entity e) {
        addEntity(e, false);
    }

    public void addEntity(Entity e, boolean primary) {
        if (primary) {
            primaryEntities.add(e);
        } else {
            entities.add(e);
        }
        addTickable(e);
    }

    public void removeEntity(Entity e) {
        removeEntity(e, false);
    }

    public void removeEntity(Entity e, boolean primary) {
        // resolve collisions
        ArrayList<EntityEntityCollision> collisions = (ArrayList<EntityEntityCollision>) e.concurrentCollisions.clone();
        for (EntityEntityCollision c : collisions) {
            c.end();
        }
        if (e instanceof Damageable) {
            ((Damageable) (e)).onColdDeath();
        }
        if (primary) {
            primaryEntities.remove(e);
        } else {
            entities.remove(e);
        }
        removeTickable(e);
    }

    public void pushNotification(NotificationBanner notification) {
        addTickable(notification);
        addRenderable(notification);
    }

    public void onNotificationFinish(NotificationBanner notification) {
        removeTickable(notification);
        removeRenderable(notification);
    }

    public void addTickable(Tickable tickable) {
        tickables.add(tickable);
    }

    public void removeTickable(Tickable tickable) {
        tickables.remove(tickable);
    }

    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
    }

    public void removeRenderable(Renderable renderable) {
        renderables.remove(renderable);
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
                changeBoundary = new MapSectorChangeBoundary(0, 0, Game.WINDOW_WIDTH - 1, BOUNDARY_SIZE, neighbor);
                break;
            case RIGHT:
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, LEFT, true);
                }
                changeBoundary = new MapSectorChangeBoundary(Game.WINDOW_WIDTH - BOUNDARY_SIZE - 1, 0, BOUNDARY_SIZE, Game.WINDOW_HEIGHT - 1, neighbor);
                break;
            case BOTTOM:
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, TOP, true);
                }
                changeBoundary = new MapSectorChangeBoundary(0, Game.WINDOW_HEIGHT - BOUNDARY_SIZE - 1, Game.WINDOW_WIDTH - 1, BOUNDARY_SIZE, neighbor);
                break;
            case LEFT:
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, RIGHT, true);
                }
                changeBoundary = new MapSectorChangeBoundary(0, 0, BOUNDARY_SIZE, Game.WINDOW_HEIGHT - 1, neighbor);
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
     * Renders all of the sector's tiles
     */
    protected void drawTiles(Graphics graphics) {
        deltaX = 0;
        deltaY = 0;
        for (int r = 0; r < tiles.size(); r++) {
            ArrayList<MapTile> row = tiles.get(r);
            for (MapTile tile : row) {
                graphics.drawImage(tile.image, deltaX, deltaY);
                deltaX += TILE_SIZE;
            }
            deltaX = 0;
            deltaY += TILE_SIZE;
        }
    }

    private void buildTiles() {
        String entireJSONString = null;
        try {
            entireJSONString = Files.readString(FileSystems.getDefault().getPath("resources", "data", "maps", parent.id, "sector-" + number + ".def"));
        } catch (IOException e) {
            Debug.error(e);
        }
        if (entireJSONString == null) {
            Debug.warning("There was a problem while building the tiles for \"" + this + "\"");
            return;
        }

        JSONObject root = new JSONObject(entireJSONString);
        JSONArray legend = root.getJSONArray("legend");
        JSONObject tilesObject = root.getJSONObject("tiles");
        ArrayList<String> tileKeys = new ArrayList<>();
        ArrayList<String> tileIDs = new ArrayList<>();
        String buildType = tilesObject.getString("type");

        for (int i = 0; i < legend.length(); i++) {
            JSONObject entry = legend.getJSONObject(i);
            tileKeys.add(entry.getString("key"));
            tileIDs.add(entry.getString("tile"));
        }

        switch (buildType.toLowerCase()) {
            case "fill":
                fill(tileIDs.get(tileKeys.indexOf(tilesObject.getString("data"))));
                break;
            case "mixed":
                fill(tileIDs.get(tileKeys.indexOf(tilesObject.getJSONObject("data").getString("common"))));
                mixed(tilesObject.getJSONObject("data").getJSONArray("custom"), tileIDs, tileKeys);
                break;
        }

        if (root.has("random")) {
            JSONArray keys = root.getJSONArray("random");
            for (int i = 0; i < keys.length(); i++) {
                String id = tileIDs.get(tileKeys.indexOf(keys.getString(i)));
                for (int r = 0; r < TILE_ROWS; r++) {
                    for (int c = 0; c < TILE_COLUMNS; c++) {
                        MapTile tile = tiles.get(r).get(c);
                        if (tile.id.equalsIgnoreCase(id)) {
                            if (Util.percentChance(0.5D)) {
                                switch (Util.randomInt(2, 0, true)) {
                                    case 0:
                                        tile.rotate();
                                        break;
                                    case 1:
                                        tile.rotate(180);
                                        break;
                                    case 2:
                                        tile.rotate(270);
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void fill(String id) {
        Image image = Images.get(Images.mapTilePath + id + ".png");
        for (int r = 0; r < TILE_ROWS; r++) {
            ArrayList<MapTile> column = new ArrayList<>();
            for (int c = 0; c < TILE_COLUMNS; c++) {
                column.add(new MapTile(id, image));
            }
            tiles.add(column);
        }
    }

    private void mixed(JSONArray custom, ArrayList<String> tileIDs, ArrayList<String> tileKeys) {
        for (int i = 0; i < custom.length(); i++) {
            JSONObject o = (JSONObject) custom.get(i);
            int c = o.getInt("c");
            int r = o.getInt("r");
            String id = tileIDs.get(tileKeys.indexOf(o.getString("key")));
            Image image = Images.get(Images.mapTilePath + id + ".png");
            if (o.has("rotate")) {
                image.rotate(o.getInt("rotate"));
            }
            tiles.get(r).set(c, new MapTile(id, image));
        }
    }

    /**
     * @return parent, sector index
     * @see #parent
     */
    public String toString() {
        return parent.toString() + ", Sector " + (parent.sectors.indexOf(this) + 1);
    }

}
