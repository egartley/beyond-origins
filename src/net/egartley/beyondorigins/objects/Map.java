package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.logic.events.MapSectorChangeEvent;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a collection of "sectors" for a map, "level", or "world"
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
     * @param id Name or identifier for the map
     */
    public Map(String id) {
        sectors = new ArrayList<>();
        this.id = id;
    }

    public abstract void tick();

    public abstract void render(Graphics graphics);

    /**
     * Changes the current sector
     *
     * @param to The new {@link MapSector} to go to
     * @see MapSector
     */
    public void changeSector(MapSector to, MapSector from) {
        onSectorChange(new MapSectorChangeEvent(from, to));
        if (currentSector != null) {
            currentSector.onPlayerLeave(to);
        }
        MapSector previousSector = currentSector;
        currentSector = to;
        currentSector.onPlayerEnter(previousSector);
    }

    /**
     * Called when the current sector changes ({@link #changeSector(MapSector)})
     */
    public void onSectorChange(MapSectorChangeEvent event) {

    }

    public String toString() {
        return id;
    }

}
