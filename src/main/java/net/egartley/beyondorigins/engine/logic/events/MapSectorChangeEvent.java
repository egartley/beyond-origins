package net.egartley.beyondorigins.engine.logic.events;

import net.egartley.beyondorigins.engine.map.MapSector;

/**
 * An event that occurs when the current map's sector is changed
 */
public class MapSectorChangeEvent {

    public MapSector goingTo;
    public MapSector comingFrom;

    public MapSectorChangeEvent(MapSector comingFrom, MapSector goingTo) {
        this.comingFrom = comingFrom;
        this.goingTo = goingTo;
    }

}
