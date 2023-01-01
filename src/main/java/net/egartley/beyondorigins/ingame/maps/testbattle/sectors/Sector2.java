package net.egartley.beyondorigins.ingame.maps.testbattle.sectors;

import net.egartley.beyondorigins.engine.map.Map;
import net.egartley.beyondorigins.engine.map.MapSector;
import net.egartley.beyondorigins.entities.Entities;
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
        updatePlayerPosition(from);
        Entities.PLAYER.generateSectorSpecificCollisions(this, true);
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
