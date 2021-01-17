package net.egartley.beyondorigins.core.logic.events;

import net.egartley.beyondorigins.core.abstracts.MapSector;

public class MapSectorChangeEvent {

    public MapSector to;
    public MapSector from;

    public MapSectorChangeEvent(MapSector from, MapSector to) {
        this.from = from;
        this.to = to;
    }

}
