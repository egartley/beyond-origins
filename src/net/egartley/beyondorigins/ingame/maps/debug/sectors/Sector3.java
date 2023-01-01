package net.egartley.beyondorigins.ingame.maps.debug.sectors;

import net.egartley.beyondorigins.engine.map.Map;
import net.egartley.beyondorigins.engine.map.MapSector;

public class Sector3 extends MapSector {

    public Sector3(Map parent) {
        super(parent, 3);
    }

    @Override
    public void init() {

    }

    @Override
    public void onPlayerEnter(MapSector from) {
        updatePlayerPosition(from);
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

    @Override
    public void setSpecialCollisions() {

    }

}
