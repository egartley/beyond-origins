package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.logic.events.MapSectorChangeEvent;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a collection of "sectors" for a specific map, or "level"
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
     * The current sector to use while rendering
     *
     * @see MapSector
     * @see MapSectorDefinition
     */
    public static MapSector currentSector;

    public Map(String id) {
        sectors = new ArrayList<>();
        this.id = id;
    }

    public abstract void tick();

    public abstract void render(Graphics graphics);

    /**
     * Method for changing the current sector
     *
     * @param sector The new {@link MapSector} to go to
     * @see MapSector
     */
    protected void changeSector(MapSector sector, MapSector comingFrom) {
        onSectorChange(new MapSectorChangeEvent(comingFrom, sector));
        if (currentSector != null)
            currentSector.onPlayerLeave(sector);
        MapSector prev = currentSector;
        currentSector = sector;
        currentSector.onPlayerEnter(prev);
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
