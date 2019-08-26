package net.egartley.beyondorigins.maps.debug.sectors;

import net.egartley.gamelib.abstracts.Map;
import net.egartley.gamelib.abstracts.MapSector;
import net.egartley.gamelib.objects.MapSectorDefinition;

public class Sector3 extends MapSector {

    public Sector3(Map parent, MapSectorDefinition def) {
        super(parent, def);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onPlayerEnter(MapSector from) {
        updatePlayerPosition(from);
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
