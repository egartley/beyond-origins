package net.egartley.beyondorigins.maps.debug;

import net.egartley.beyondorigins.engine.Direction;
import net.egartley.beyondorigins.engine.LevelMap;
import net.egartley.beyondorigins.engine.LevelMapSector;
import net.egartley.beyondorigins.engine.NeighborMapping;

public class DebugMap extends LevelMap {

    public DebugMap() {
        super("debug");
        LevelMapSector s1 = new Sector1(this);
        LevelMapSector s2 = new Sector2(this);
        s1.buildTiles();
        s2.buildTiles();
        addSector(s1);
        addSector(s2);
        addNeighborMapping(new NeighborMapping(s1.id, Direction.LEFT, s2.id));

        setSector(s1.id);
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
