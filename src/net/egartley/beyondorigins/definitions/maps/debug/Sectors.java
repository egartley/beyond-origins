package net.egartley.beyondorigins.definitions.maps.debug;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.maps.TileBuilder;
import net.egartley.beyondorigins.objects.MapSectorDefinition;
import net.egartley.beyondorigins.objects.MapTile;

import java.util.ArrayList;

public class Sectors {

    public static MapSectorDefinition sector1;

    public static void define() {
        sector1 = new MapSectorDefinition(TileBuilder.buildArrayList(MapTile.GRASS, 17, 31));

        // randomly rotate tiles
        for (ArrayList<MapTile> row : sector1.tiles) {
            for (MapTile tile : row) {
                if (Util.randomInt(10, 1, true) > 5) {
                    tile.rotate();
                    if (Util.randomInt(10, 1, true) > 5) {
                        // rotate again, why not
                        tile.rotate();
                    }
                }
            }
        }

        // test rotation by creating a "circular" path
        sector1.tiles.get(5).set(11, MapTile.get(MapTile.GRASS_PATH_2));
        sector1.tiles.get(5).set(19, MapTile.get(MapTile.GRASS_PATH_2));
        sector1.tiles.get(9).set(11, MapTile.get(MapTile.GRASS_PATH_2));
        sector1.tiles.get(9).set(19, MapTile.get(MapTile.GRASS_PATH_2));
        sector1.tiles.get(5).get(19).rotate();
        sector1.tiles.get(9).get(19).rotate(Math.PI);
        sector1.tiles.get(9).get(11).rotate(1.5D * Math.PI);
        // can't be bothered to copy and paste, so use for loops...
        for (int i = 12; i <= 18; i++) {
            sector1.tiles.get(5).set(i, MapTile.get(MapTile.GRASS_PATH_1));
            sector1.tiles.get(5).get(i).rotate();
        }
        for (int i = 12; i <= 18; i++) {
            sector1.tiles.get(9).set(i, MapTile.get(MapTile.GRASS_PATH_1));
            sector1.tiles.get(9).get(i).rotate();
        }
        for (int i = 6; i <= 8; i++) {
            sector1.tiles.get(i).set(11, MapTile.get(MapTile.GRASS_PATH_1));
        }
        for (int i = 6; i <= 8; i++) {
            sector1.tiles.get(i).set(19, MapTile.get(MapTile.GRASS_PATH_1));
        }
    }

}
