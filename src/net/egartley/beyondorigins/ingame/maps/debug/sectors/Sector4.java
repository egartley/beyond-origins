package net.egartley.beyondorigins.ingame.maps.debug.sectors;

import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.abstracts.MapSector;

public class Sector4 extends MapSector {

    public Sector4(Map parent) {
        super(parent, 4);
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
