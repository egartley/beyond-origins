package net.egartley.beyondorigins.logic.events;

import net.egartley.beyondorigins.objects.MapSector;

/**
 * A custom "event" that can be used for gathering information about a sector change
 */
public class MapSectorChangeEvent {

    public MapSector from;
    public MapSector to;

    /**
     * Creates a new event that can be used for gathering information about a sector change
     *
     * @param from
     *         The sector that the player came from
     * @param to
     *         The sector that the player went to
     */
    public MapSectorChangeEvent(MapSector from, MapSector to) {
        this.from = from;
        this.to = to;
    }

}
