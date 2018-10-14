package net.egartley.beyondorigins.definitions.maps.testmap;

import net.egartley.beyondorigins.maps.TileBuilder;
import net.egartley.beyondorigins.objects.MapSectorDefinition;

/**
 * Sector definitions for TestMap
 */
public class Sectors {

    public static MapSectorDefinition blankGrass;

    public static void define() {
        blankGrass = new MapSectorDefinition(TileBuilder.buildArrayList(TileBuilder.GRASS, 17, 31));
    }

}
