package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.logic.events.MapSectorChangeEvent;

import java.awt.*;
import java.util.ArrayList;

/**
 * Collection of "sectors" that make up a map, like a "level", or "world"
 */
public abstract class Map {

    /**
     * Collection of all the possible sectors
     *
     * @see MapSector
     * @see MapSectorDefinition
     */
    public ArrayList<MapSector> sectors;
    /**
     * Human-readable identifier
     */
    private String id;
    /**
     * The sector the player is currently in
     *
     * @see MapSector
     */
    public static MapSector currentSector;

    /**
     * Creates a new map, with no sectors
     *
     * @param id
     *         Name or identifier for the map
     */
    public Map(String id) {
        sectors = new ArrayList<>();
        this.id = id;
    }

    public abstract void tick();

    public abstract void render(Graphics graphics);

    /**
     * Called when the current sector changes ({@link #changeSector(MapSector, MapSector)})
     */
    public abstract void onSectorChange(MapSectorChangeEvent event);

    /**
     * Changes the current sector
     *
     * @param to
     *         The sector to move to
     *
     * @see MapSector
     */
    public void changeSector(MapSector to, MapSector from) {
        onSectorChange(new MapSectorChangeEvent(from, to));
        if (currentSector != null) {
            currentSector.onPlayerLeave_internal(to);
            currentSector.onPlayerLeave(to);
        }
        MapSector previous = currentSector;
        currentSector = to;
        currentSector.onPlayerEnter_internal(previous);
        currentSector.onPlayerEnter(previous);
    }

    public String toString() {
        return id;
    }

}
