package net.egartley.beyondorigins.core.logic.events;

import net.egartley.beyondorigins.core.abstracts.MapSector;

/**
 * An event that occurs when the current map's sector is changed
 */
public class MapSectorChangeEvent {

    public MapSector to;
    public MapSector from;

    public MapSectorChangeEvent(MapSector from, MapSector to) {
        this.from = from;
        this.to = to;
    }

}
