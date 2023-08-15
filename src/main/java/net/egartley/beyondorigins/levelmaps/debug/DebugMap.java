package net.egartley.beyondorigins.levelmaps.debug;

import net.egartley.beyondorigins.engine.LevelMap;
import net.egartley.beyondorigins.engine.LevelMapSector;

public class DebugMap extends LevelMap {

    public DebugMap() {
        super("debug");
        addSector(new Sector1(this));
        setSector(getSector(1));
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }

    @Override
    public void onSectorChange(LevelMapSector from, LevelMapSector to) {

    }

}
