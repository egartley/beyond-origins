package net.egartley.beyondorigins.maps;

import net.egartley.beyondorigins.Util;
import net.egartley.gamelib.objects.MapSector;
import net.egartley.gamelib.objects.MapTile;

import java.util.ArrayList;

public class TileBuilder {

    public static ArrayList<ArrayList<MapTile>> build(MapTile tile) {
        ArrayList<ArrayList<MapTile>> array = new ArrayList<>();
        for (int r = 0; r < MapSector.TILE_COLUMNS; r++) {
            array.add(new ArrayList<>());
            for (int c = 0; c < MapSector.TILE_ROWS; c++) {
                array.get(r).add(tile);
            }
        }
        return array;
    }

    public static ArrayList<ArrayList<MapTile>> buildRandom(MapTile tile) {
        ArrayList<ArrayList<MapTile>> array = new ArrayList<>();
        for (int r = 0; r < MapSector.TILE_COLUMNS; r++) {
            array.add(new ArrayList<>());
            for (int c = 0; c < MapSector.TILE_ROWS; c++) {
                if (Util.fiftyFifty()) {
                    array.get(r).add(tile);
                } else {
                    array.get(r).add(tile.rotate());
                }
            }
        }
        return array;
    }

}
