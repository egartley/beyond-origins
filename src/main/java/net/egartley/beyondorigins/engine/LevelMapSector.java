package net.egartley.beyondorigins.engine;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class LevelMapSector implements Tickable {

    public int id;
    public static final short TILE_ROWS = 17, TILE_COLS = 30;
    private int tileRenderX, tileRenderY;
    private final byte MAX_NEIGHBORS = 4;
    private final short TILE_SIZE = 32, BOUNDARY_SIZE = 18;
    private final short PLAYER_ENTRANCE_OFFSET = BOUNDARY_SIZE + 4;

    protected LevelMap parent;
    protected ArrayList<ArrayList<LevelMapTile>> tiles = new ArrayList<>();
    private HashMap<Direction, LevelMapSector> neighbors;

    public LevelMapSector(LevelMap parent, int id) {
        this.parent = parent;
        this.id = id;
        neighbors = new HashMap<>();
        buildTiles();
    }

    public abstract void init();

    public void render(Graphics graphics) {
        drawTiles(graphics);
    }

    public void onEnter(LevelMapSector from) {
        init();
    }

    public void onLeave(LevelMapSector to) {
        // entities.clear();
        // Collisions.nuke();
    }

    protected void drawTiles(Graphics graphics) {
        tileRenderX = 0;
        tileRenderY = 0;
        for (ArrayList<LevelMapTile> row : tiles) {
            for (LevelMapTile tile : row) {
                graphics.drawImage(tile.image, tileRenderX, tileRenderY);
                tileRenderX += TILE_SIZE;
            }
            tileRenderX = 0;
            tileRenderY += TILE_SIZE;
        }
    }

    protected void updatePlayerPosition(LevelMapSector from) {
        /*switch (getNeighborDirection(from)) {
            case Direction.UP -> Entities.PLAYER.y(PLAYER_ENTRANCE_OFFSET);
            case Direction.LEFT -> Entities.PLAYER.x(PLAYER_ENTRANCE_OFFSET);
            case Direction.DOWN -> Entities.PLAYER.y(Game.WINDOW_HEIGHT - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.height);
            case Direction.RIGHT -> Entities.PLAYER.x(Game.WINDOW_WIDTH - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.width);
        }*/
    }

    private Direction getNeighborDirection(LevelMapSector neighbor) {
        for (java.util.Map.Entry<Direction, LevelMapSector> entry : neighbors.entrySet()) {
            if (entry.getValue() == neighbor) {
                return entry.getKey();
            }
        }
        System.out.println("WARNING: Unable to get direction for neighbor \"" + neighbor + "\" of \"" + this + "\"!");
        return Direction.DOWN;
    }

    public void setNeighborAt(LevelMapSector neighbor, Direction direction) {
        setNeighborAt(neighbor, direction, false);
    }

    private void setNeighborAt(LevelMapSector neighbor, Direction direction, boolean didSetInverse) {
        /*MapSectorChangeBoundary changeBoundary = null;
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
                    System.out.println("WARNING: Unknown direction while setting a sector neighbor! (" + direction + ")");
        }
        if (changeBoundary != null) {
            changeBoundaries.add(changeBoundary);
            changeCollisions.add(new MapSectorChangeCollision(changeBoundary, Entities.PLAYER.boundary, changeBoundary.goingTo, this, parent));
        } else {
            System.out.println("WARNING: Could not set neighbor \"" + neighbor + "\" for \"" + this + "\"!");
        }
        neighbors.put(direction, neighbor);*/
    }

    private void buildTiles() {
        String entireJSONString = null;
        try {
            entireJSONString = Files.readString(FileSystems.getDefault().getPath("src", "main",
                    "resources", "data", "maps", parent.name, "sector-" + id + ".def"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (entireJSONString == null) {
            System.out.println("WARNING: Issue while building the tiles for \"" + this + "\" (JSON string was null)");
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
                    for (int c = 0; c < TILE_COLS; c++) {
                        LevelMapTile tile = tiles.get(r).get(c);
                        if (tile.getID().equalsIgnoreCase(id)) {
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
        Image image;
        try {
            image = new Image("images/map-tiles/" + id + ".png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
        for (int r = 0; r < TILE_ROWS; r++) {
            ArrayList<LevelMapTile> column = new ArrayList<>();
            for (int c = 0; c < TILE_COLS; c++) {
                column.add(new LevelMapTile(id, image));
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
            Image image;
            try {
                image = new Image("images/map-tiles/" + id + ".png");
            } catch (SlickException e) {
                throw new RuntimeException(e);
            }
            if (tileObject.has("rotate")) {
                image.rotate(tileObject.getInt("rotate"));
            }
            tiles.get(row).set(column, new LevelMapTile(id, image));
        }
    }

    @Override
    public String toString() {
        return parent.toString() + ", Sector " + (parent.getSectors().indexOf(this) + 1);
    }

}
