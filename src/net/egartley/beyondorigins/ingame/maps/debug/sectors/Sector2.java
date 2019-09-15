package net.egartley.beyondorigins.ingame.maps.debug.sectors;

import net.egartley.gamelib.abstracts.Map;
import net.egartley.gamelib.abstracts.MapSector;

public class Sector2 extends MapSector {

    public Sector2(Map parent) {
        super(parent, 2);
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
