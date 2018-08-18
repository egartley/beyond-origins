package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.logic.events.MapSectorChangeEvent;

import java.awt.*;
import java.util.ArrayList;

/**
 * Collection of "sectors" that make up a map, which is like a level or world
 */
public abstract class Map {

    /**
     * Human-readable identifier
     */
    private String id;
    /**
     * Collection of the map's sectors
     */
    public ArrayList<MapSector> sectors;
    /**
     * The sector where the player is currently
     */
    public static MapSector currentSector;

    /**
     * Creates a new map, and initializes {@link #sectors} with {@link ArrayList#ArrayList() ArrayList<>()}
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
     * Moves from one sector to another (can also be used when initially entering the map)
     *
     * @param to
     *         Where the player is going
     * @param from
     *         Where the player is coming from (<code>null</code> if initially entering the map)
     *
     * @see #onSectorChange(MapSectorChangeEvent)
     * @see MapSector#onPlayerEnter(MapSector)
     * @see MapSector#onPlayerLeave(MapSector)
     */
    public void changeSector(MapSector to, MapSector from) {
        onSectorChange(new MapSectorChangeEvent(from, to));
        if (currentSector != null) {
            currentSector.onPlayerLeave_internal();
            currentSector.onPlayerLeave(to);
        }
        MapSector previous = currentSector;
        currentSector = to;
        currentSector.onPlayerEnter_internal();
        currentSector.onPlayerEnter(previous);
    }

    public String toString() {
        return id;
    }

}
