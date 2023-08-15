package net.egartley.beyondorigins.levelmaps.debug;

import net.egartley.beyondorigins.engine.LevelMap;
import net.egartley.beyondorigins.engine.LevelMapSector;

public class DebugMap extends LevelMap {

    public DebugMap() {
        super("Debug");
        addSector(new Sector1(this));
        setSector(getSector(1));
    }

    @Override
    public void tick() {
        currentSector.tick();
    }

    @Override
    public void onEnter() {
        setSector(getSectors().get(0));
    }

    @Override
    public void onLeave() {

    }

    @Override
    public void onSectorChange(LevelMapSector from, LevelMapSector to) {

    }

}
