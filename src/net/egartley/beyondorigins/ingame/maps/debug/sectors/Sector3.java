package net.egartley.beyondorigins.ingame.maps.debug.sectors;

import net.egartley.gamelib.abstracts.Map;
import net.egartley.gamelib.abstracts.MapSector;

public class Sector3 extends MapSector {

    public Sector3(Map parent) {
        super(parent, 3);
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
