package net.egartley.beyondorigins.ingame.maps.testbattle.sectors;

import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.abstracts.MapSector;
import net.egartley.beyondorigins.ingame.maps.testbattle.TestBattleMap;

public class Sector2 extends MapSector {

    public Sector2(Map parent) {
        super(parent, 2);
    }

    @Override
    public void init() {

    }

    @Override
    public void setSpecialCollisions() {

    }

    @Override
    public void onPlayerEnter(MapSector from) {
        ((TestBattleMap) parent).spawnFHBoss();
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
