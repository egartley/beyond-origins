package net.egartley.beyondorigins.definitions.maps.testmap;

import net.egartley.beyondorigins.maps.TileBuilder;
import net.egartley.beyondorigins.objects.MapSectorDefinition;
import net.egartley.beyondorigins.objects.MapTile;

import java.util.ArrayList;

/**
 * Sector definitions for TestMap
 */
public class Sectors {

    public static MapSectorDefinition sector1;
    public static MapSectorDefinition sector2;
    public static MapSectorDefinition sector3;

    public static void define() {
        ArrayList<ArrayList<MapTile>> grass = TileBuilder.buildArrayList(TileBuilder.GRASS, 17, 31);
        sector1 = new MapSectorDefinition(grass);
        sector2 = new MapSectorDefinition(grass);
        sector3 = new MapSectorDefinition(grass);
    }

}
