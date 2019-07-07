package net.egartley.beyondorigins.definitions.maps.debug;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.maps.TileBuilder;
import net.egartley.gamelib.objects.MapSector;
import net.egartley.gamelib.objects.MapSectorDefinition;
import net.egartley.gamelib.objects.MapTile;

import java.util.ArrayList;

public class Sectors {

    public static MapSectorDefinition sector1, sector2, sector3, sector4;

    public static void define() {
        // ----------- SECTOR 1 -----------
        sector1 = new MapSectorDefinition(TileBuilder.buildArrayList(MapTile.GRASS, MapSector.TILE_COLUMNS, MapSector.TILE_ROWS));
        for (ArrayList<MapTile> row : sector1.tiles) {
            for (MapTile tile : row) {
                if (Util.randomInt(10, 1, true) > 5) {
                    tile.rotate();
                }
            }
        }
        sector1.tiles.get(0).set(26, MapTile.get(MapTile.GRASS_PATH_1));
        sector1.tiles.get(1).set(26, MapTile.get(MapTile.GRASS_PATH_1));
        sector1.tiles.get(2).set(26, MapTile.get(MapTile.GRASS_PATH_1));
        sector1.tiles.get(3).set(26, MapTile.get(MapTile.GRASS_PATH_2));
        sector1.tiles.get(3).set(27, MapTile.get(MapTile.GRASS_PATH_1));
        sector1.tiles.get(3).set(28, MapTile.get(MapTile.GRASS_PATH_1));
        sector1.tiles.get(3).set(29, MapTile.get(MapTile.GRASS_PATH_1));
        sector1.getTile(3, 26).rotate(1.5D * Math.PI);
        sector1.getTile(3, 27).rotate();
        sector1.getTile(3, 28).rotate();
        sector1.getTile(3, 29).rotate();
        // ----------- SECTOR 1 -----------

        // ----------- SECTOR 2 -----------
        sector2 = new MapSectorDefinition(TileBuilder.buildArrayList(MapTile.GRASS, MapSector.TILE_COLUMNS, MapSector.TILE_ROWS));
        for (ArrayList<MapTile> row : sector2.tiles) {
            for (MapTile tile : row) {
                if (Util.randomInt(10, 1, true) > 5) {
                    tile.rotate();
                }
            }
        }
        sector2.tiles.get(14).set(26, MapTile.get(MapTile.GRASS_PATH_1));
        sector2.tiles.get(15).set(26, MapTile.get(MapTile.GRASS_PATH_1));
        sector2.tiles.get(16).set(26, MapTile.get(MapTile.GRASS_PATH_1));
        sector2.tiles.get(13).set(26, MapTile.get(MapTile.GRASS_PATH_2));
        sector2.tiles.get(13).set(27, MapTile.get(MapTile.GRASS_PATH_1));
        sector2.tiles.get(13).set(28, MapTile.get(MapTile.GRASS_PATH_1));
        sector2.tiles.get(13).set(29, MapTile.get(MapTile.GRASS_PATH_1));
        sector2.getTile(13, 27).rotate();
        sector2.getTile(13, 28).rotate();
        sector2.getTile(13, 29).rotate();
        // ----------- SECTOR 2 -----------

        // ----------- SECTOR 3 -----------
        sector3 = new MapSectorDefinition(TileBuilder.buildArrayList(MapTile.GRASS, MapSector.TILE_COLUMNS, MapSector.TILE_ROWS));
        for (ArrayList<MapTile> row : sector3.tiles) {
            for (MapTile tile : row) {
                if (Util.randomInt(10, 1, true) > 5) {
                    tile.rotate();
                }
            }
        }
        sector3.tiles.get(13).set(0, MapTile.get(MapTile.GRASS_PATH_1));
        sector3.tiles.get(13).set(1, MapTile.get(MapTile.GRASS_PATH_1));
        sector3.tiles.get(13).set(2, MapTile.get(MapTile.GRASS_PATH_1));
        sector3.tiles.get(13).set(3, MapTile.get(MapTile.GRASS_PATH_2));
        sector3.tiles.get(14).set(3, MapTile.get(MapTile.GRASS_PATH_1));
        sector3.tiles.get(15).set(3, MapTile.get(MapTile.GRASS_PATH_1));
        sector3.tiles.get(16).set(3, MapTile.get(MapTile.GRASS_PATH_1));
        sector3.getTile(13, 3).rotate();
        sector3.getTile(13, 0).rotate();
        sector3.getTile(13, 1).rotate();
        sector3.getTile(13, 2).rotate();
        // ----------- SECTOR 3 -----------

        // ----------- SECTOR 4 -----------
        sector4 = new MapSectorDefinition(TileBuilder.buildArrayList(MapTile.GRASS, MapSector.TILE_COLUMNS, MapSector.TILE_ROWS));
        for (ArrayList<MapTile> row : sector4.tiles) {
            for (MapTile tile : row) {
                if (Util.randomInt(10, 1, true) > 5) {
                    tile.rotate();
                }
            }
        }
        sector4.tiles.get(3).set(0, MapTile.get(MapTile.GRASS_PATH_1));
        sector4.tiles.get(3).set(1, MapTile.get(MapTile.GRASS_PATH_1));
        sector4.tiles.get(3).set(2, MapTile.get(MapTile.GRASS_PATH_1));
        sector4.tiles.get(3).set(3, MapTile.get(MapTile.GRASS_PATH_2));
        sector4.tiles.get(0).set(3, MapTile.get(MapTile.GRASS_PATH_1));
        sector4.tiles.get(1).set(3, MapTile.get(MapTile.GRASS_PATH_1));
        sector4.tiles.get(2).set(3, MapTile.get(MapTile.GRASS_PATH_1));
        sector4.getTile(3, 3).rotate(Math.PI);
        sector4.getTile(3, 0).rotate();
        sector4.getTile(3, 1).rotate();
        sector4.getTile(3, 2).rotate();
        // ----------- SECTOR 4 -----------
    }

}
