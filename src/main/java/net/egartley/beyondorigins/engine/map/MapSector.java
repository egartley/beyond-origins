package net.egartley.beyondorigins.engine.map;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.entities.Entity;
import net.egartley.beyondorigins.engine.enums.Direction;
import net.egartley.beyondorigins.engine.graphics.MapTile;
import net.egartley.beyondorigins.engine.interfaces.Damageable;
import net.egartley.beyondorigins.engine.interfaces.Renderable;
import net.egartley.beyondorigins.engine.interfaces.Tickable;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.MapSectorChangeCollision;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.MapSectorChangeBoundary;
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
import java.util.HashMap;

/**
 * Specific part, or area, of a map that fills the entire window
 */
public abstract class MapSector implements Tickable, Renderable {

    private int deltaX;
    private int deltaY;
    private ArrayList<Entity> removeList, addList;
    private final byte MAX_NEIGHBORS = 4;
    private final short TILE_SIZE = 32;
    private final short BOUNDARY_SIZE = 18;
    private final short PLAYER_ENTRANCE_OFFSET = BOUNDARY_SIZE + 4;
    private final HashMap<Direction, MapSector> neighbors = new HashMap<>();
    private final ArrayList<MapSectorChangeCollision> changeCollisions = new ArrayList<>();

    protected Map parent;
    protected ArrayList<ArrayList<MapTile>> tiles = new ArrayList<>();
    protected ArrayList<MapSectorChangeBoundary> changeBoundaries = new ArrayList<>();

    public int number;
    public static final short TILE_ROWS = 17;
    public static final short TILE_COLUMNS = 30;
    public ArrayList<Entity> entities = new ArrayList<>();

    public MapSector(Map parent, int number) {
        this.parent = parent;
        this.number = number;
        buildTiles();
        removeList = new ArrayList<>();
        addList = new ArrayList<>();
    }

    public abstract void init();

    public abstract void setSpecialCollisions();

    public abstract void onPlayerEnter(MapSector from);

    public abstract void onPlayerLeave(MapSector to);

    protected void drawTiles(Graphics graphics) {
        deltaX = 0;
        deltaY = 0;
        for (ArrayList<MapTile> row : tiles) {
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
            entireJSONString = Files.readString(FileSystems.getDefault().getPath("src", "main",
                    "resources", "data", "maps", parent.name, "sector-" + number + ".def"));
        } catch (IOException e) {
            Debug.error(e);
        }
        if (entireJSONString == null) {
            Debug.warning("There was a problem while building the tiles for \"" + this + "\" (JSON string was null)");
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
            case "fill" -> fill(tileIDs.get(tileKeys.indexOf(tilesObject.getString("data"))));
            case "mixed" -> {
                fill(tileIDs.get(tileKeys.indexOf(tilesObject.getJSONObject("data").getString("common"))));
                mixed(tilesObject.getJSONObject("data").getJSONArray("custom"), tileIDs, tileKeys);
            }
        }
        if (root.has("random")) {
            JSONArray keys = root.getJSONArray("random");
            for (int i = 0; i < keys.length(); i++) {
                String id = tileIDs.get(tileKeys.indexOf(keys.getString(i)));
                for (int r = 0; r < TILE_ROWS; r++) {
                    for (int c = 0; c < TILE_COLUMNS; c++) {
                        MapTile tile = tiles.get(r).get(c);
                        if (tile.id.equalsIgnoreCase(id)) {
                            if (Util.fiftyFifty()) {
                                switch (Util.randomInt(0, 2, true)) {
                                    case 0 -> tile.rotate();
                                    case 1 -> tile.rotate(180);
                                    case 2 -> tile.rotate(270);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void fill(String id) {
        Image image = Images.getImageFromPath(Images.mapTilePath + id + ".png");
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
            JSONObject tileObject = (JSONObject) custom.get(i);
            int row = tileObject.getInt("r");
            int column = tileObject.getInt("c");
            String id = tileIDs.get(tileKeys.indexOf(tileObject.getString("key")));
            Image image = Images.getImageFromPath(Images.mapTilePath + id + ".png");
            if (tileObject.has("rotate")) {
                image.rotate(tileObject.getInt("rotate"));
            }
            tiles.get(row).set(column, new MapTile(id, image));
        }
    }

    public void onPlayerEnter_internal() {
        init();
    }

    public void onPlayerLeave_internal() {
        entities.clear();
        Collisions.nuke();
    }

    protected void updatePlayerPosition(MapSector from) {
        switch (getNeighborDirection(from)) {
            case UP -> Entities.PLAYER.y(PLAYER_ENTRANCE_OFFSET);
            case LEFT -> Entities.PLAYER.x(PLAYER_ENTRANCE_OFFSET);
            case DOWN -> Entities.PLAYER.y(Game.WINDOW_HEIGHT - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.height);
            case RIGHT -> Entities.PLAYER.x(Game.WINDOW_WIDTH - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.width);
        }
    }

    public void addEntityDirect(Entity e) {
        entities.add(e);
    }

    public void removeEntityDirect(Entity e) {
        entities.remove(e);
    }

    public void addEntity(Entity e) {
        addList.add(e);
    }

    public void removeEntity(Entity e) {
        Collisions.endAllWith(e);
        Collisions.removeAllWith(e);
        if (e instanceof Damageable de) {
            de.onColdDeath();
        }
        removeList.add(e);
    }

    public void setNeighborAt(MapSector neighbor, Direction direction) {
        setNeighborAt(neighbor, direction, false);
    }

    private void setNeighborAt(MapSector neighbor, Direction direction, boolean didSetInverse) {
        MapSectorChangeBoundary changeBoundary = null;
        switch (direction) {
            case UP -> {
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, Direction.DOWN, true);
                }
                changeBoundary = new MapSectorChangeBoundary(0, 0, Game.WINDOW_WIDTH - 1, BOUNDARY_SIZE, neighbor);
            }
            case RIGHT -> {
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, Direction.LEFT, true);
                }
                changeBoundary = new MapSectorChangeBoundary(Game.WINDOW_WIDTH - BOUNDARY_SIZE - 1, 0, BOUNDARY_SIZE, Game.WINDOW_HEIGHT - 1, neighbor);
            }
            case DOWN -> {
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, Direction.UP, true);
                }
                changeBoundary = new MapSectorChangeBoundary(0, Game.WINDOW_HEIGHT - BOUNDARY_SIZE - 1, Game.WINDOW_WIDTH - 1, BOUNDARY_SIZE, neighbor);
            }
            case LEFT -> {
                if (!didSetInverse) {
                    neighbor.setNeighborAt(this, Direction.RIGHT, true);
                }
                changeBoundary = new MapSectorChangeBoundary(0, 0, BOUNDARY_SIZE, Game.WINDOW_HEIGHT - 1, neighbor);
            }
            default ->
                    Debug.warning("Unknown direction specified while attempting to set a sector neighbor! (" + direction + ")");
        }
        if (changeBoundary != null) {
            changeBoundaries.add(changeBoundary);
            changeCollisions.add(new MapSectorChangeCollision(changeBoundary, Entities.PLAYER.boundary, changeBoundary.goingTo, this, parent));
        } else {
            Debug.warning("Could not set a neighbor (\"" + neighbor + "\") for \"" + this + "\"!");
        }
        neighbors.put(direction, neighbor);
    }

    protected void updateEntityList() {
        entities.addAll(addList);
        for (Entity e : removeList) {
            entities.remove(e);
        }
        addList.clear();
        removeList.clear();
    }

    @Override
    public void render(Graphics graphics) {
        drawTiles(graphics);
        updateEntityList();
        for (Entity e : entities) {
            if (!e.isRenderPlayerBased || e.isRenderingBelowPlayer) {
                e.render(graphics);
            }
        }
        Entities.PLAYER.render(graphics);
        for (Entity e : entities) {
            if (e.isRenderPlayerBased && !e.isRenderingBelowPlayer) {
                e.render(graphics);
            }
        }
        if (Game.debug) {
            changeBoundaries.forEach(boundary -> boundary.render(graphics));
        }
    }

    @Override
    public void tick() {
        updateEntityList();
        for (Entity e : entities) {
            e.tick();
        }
        Entities.PLAYER.tick();
        changeCollisions.forEach(MapSectorChangeCollision::tick);
    }

    private Direction getNeighborDirection(MapSector neighbor) {
        for (java.util.Map.Entry<Direction, MapSector> entry : neighbors.entrySet()) {
            if (entry.getValue() == neighbor) {
                return entry.getKey();
            }
        }
        Debug.warning("Unable to get the direction for neighbor \"" + neighbor + "\" of \"" + this + "\"!");
        return Direction.DOWN;
    }

    public String toString() {
        return parent.toString() + ", Sector " + (parent.sectors.indexOf(this) + 1);
    }

}
