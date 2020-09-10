package net.egartley.beyondorigins.ingame.maps.testbattle.sectors;

import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.abstracts.MapSector;
import net.egartley.beyondorigins.entities.Entities;

public class Sector1 extends MapSector {

    public Sector1(Map parent) {
        super(parent, 1);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        Entities.PLAYER.setPosition(300, 300);
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
