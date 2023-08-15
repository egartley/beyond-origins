package net.egartley.beyondorigins.engine;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;

public class TileBuilder {

    private final short TILE_ROWS = 17, TILE_COLS = 30;

    protected ArrayList<ArrayList<LevelMapTile>> buildTiles(LevelMapSector sector) {
        ArrayList<ArrayList<LevelMapTile>> tiles = new ArrayList<>();
        String entireJSONString = null;
        try {
            entireJSONString = Files.readString(FileSystems.getDefault().getPath("src", "main",
                    "resources", "data", "maps", sector.parent.name, "sector-" + sector.id + ".def"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (entireJSONString == null) {
            System.out.println("WARNING: Issue while building the tiles for \"" + this + "\" (JSON string was null)");
            return tiles;
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
            case "fill" -> fill(tileIDs.get(tileKeys.indexOf(tilesObject.getString("data"))), tiles);
            case "mixed" -> {
                fill(tileIDs.get(tileKeys.indexOf(tilesObject.getJSONObject("data").getString("common"))), tiles);
                mixed(tilesObject.getJSONObject("data").getJSONArray("custom"), tileIDs, tileKeys, tiles);
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
        return tiles;
    }

    private void fill(String id, ArrayList<ArrayList<LevelMapTile>> tiles) {
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

    private void mixed(JSONArray custom, ArrayList<String> tileIDs, ArrayList<String> tileKeys, ArrayList<ArrayList<LevelMapTile>> tiles) {
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

}
