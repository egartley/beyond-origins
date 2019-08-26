package net.egartley.beyondorigins.definitions.maps.debug;

import net.egartley.beyondorigins.maps.TileBuilder;
import net.egartley.gamelib.graphics.MapTile;
import net.egartley.gamelib.objects.MapSectorDefinition;

public class Sectors {

    public static MapSectorDefinition sector1, sector2, sector3, sector4;

    public static void define() {
        // ----------- SECTOR 1 -----------
        sector1 = new MapSectorDefinition(TileBuilder.buildRandom(MapTile.GRASS));
        sector1.tiles.get(0).set(26, MapTile.GRASS_PATH_1);
        sector1.tiles.get(1).set(26, MapTile.GRASS_PATH_1);
        sector1.tiles.get(2).set(26, MapTile.GRASS_PATH_1);
        sector1.tiles.get(3).set(26, MapTile.GRASS_PATH_2.rotate(1.5D * Math.PI));
        sector1.tiles.get(3).set(27, MapTile.GRASS_PATH_1.rotate());
        sector1.tiles.get(3).set(28, MapTile.GRASS_PATH_1.rotate());
        sector1.tiles.get(3).set(29, MapTile.GRASS_PATH_1.rotate());
        // ----------- SECTOR 1 -----------

        // ----------- SECTOR 2 -----------
        sector2 = new MapSectorDefinition(TileBuilder.buildRandom(MapTile.GRASS));
        sector2.tiles.get(14).set(26, MapTile.GRASS_PATH_1);
        sector2.tiles.get(15).set(26, MapTile.GRASS_PATH_1);
        sector2.tiles.get(16).set(26, MapTile.GRASS_PATH_1);
        sector2.tiles.get(13).set(26, MapTile.GRASS_PATH_2);
        sector2.tiles.get(13).set(27, MapTile.GRASS_PATH_1.rotate());
        sector2.tiles.get(13).set(28, MapTile.GRASS_PATH_1.rotate());
        sector2.tiles.get(13).set(29, MapTile.GRASS_PATH_1.rotate());
        // ----------- SECTOR 2 -----------

        // ----------- SECTOR 3 -----------
        sector3 = new MapSectorDefinition(TileBuilder.buildRandom(MapTile.GRASS));
        sector3.tiles.get(13).set(0, MapTile.GRASS_PATH_1.rotate());
        sector3.tiles.get(13).set(1, MapTile.GRASS_PATH_1.rotate());
        sector3.tiles.get(13).set(2, MapTile.GRASS_PATH_1.rotate());
        sector3.tiles.get(13).set(3, MapTile.GRASS_PATH_2.rotate());
        sector3.tiles.get(14).set(3, MapTile.GRASS_PATH_1);
        sector3.tiles.get(15).set(3, MapTile.GRASS_PATH_1);
        sector3.tiles.get(16).set(3, MapTile.GRASS_PATH_1);
        // ----------- SECTOR 3 -----------

        // ----------- SECTOR 4 -----------
        sector4 = new MapSectorDefinition(TileBuilder.buildRandom(MapTile.GRASS));
        sector4.tiles.get(3).set(0, MapTile.GRASS_PATH_1.rotate());
        sector4.tiles.get(3).set(1, MapTile.GRASS_PATH_1.rotate());
        sector4.tiles.get(3).set(2, MapTile.GRASS_PATH_1.rotate());
        sector4.tiles.get(3).set(3, MapTile.GRASS_PATH_2.rotate(Math.PI));
        sector4.tiles.get(0).set(3, MapTile.GRASS_PATH_1);
        sector4.tiles.get(1).set(3, MapTile.GRASS_PATH_1);
        sector4.tiles.get(2).set(3, MapTile.GRASS_PATH_1);
        // ----------- SECTOR 4 -----------
    }

}
