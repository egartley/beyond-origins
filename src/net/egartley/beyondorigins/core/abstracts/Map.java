package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.core.interfaces.Tickable;
import net.egartley.beyondorigins.core.logic.events.MapSectorChangeEvent;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

/**
 * Collection of "sectors" that make up a map, which is like a level or world
 */
public abstract class Map implements Tickable {

    /**
     * Identifier for the map
     */
    public String id;
    /**
     * Collection of the map's sectors
     */
    public ArrayList<MapSector> sectors;
    /**
     * The current sector
     */
    public MapSector sector;

    /**
     * Creates a new map
     *
     * @param id Name or identifier for the map
     */
    public Map(String id) {
        sectors = new ArrayList<>();
        this.id = id;
    }

    /**
     * Renders the map's contents
     *
     * @param graphics The graphics to use
     */
    public abstract void render(Graphics graphics);

    /**
     * Called when the player enters the map
     */
    public abstract void onPlayerEnter();

    /**
     * Called when the player leaves the map
     */
    public abstract void onPlayerLeave();

    /**
     * Called when the current sector changes
     *
     * @see #changeSector(MapSector, MapSector)
     */
    public abstract void onSectorChange(MapSectorChangeEvent event);

    /**
     * Moves from one sector to another
     *
     * @param to   Where the player is going
     * @param from Where the player is coming from (null if initially entering the map)
     * @see #onSectorChange(MapSectorChangeEvent)
     * @see MapSector#onPlayerEnter(MapSector)
     * @see MapSector#onPlayerLeave(MapSector)
     */
    public void changeSector(MapSector to, MapSector from) {
        onSectorChange(new MapSectorChangeEvent(from, to));
        if (sector != null) {
            sector.onPlayerLeave_internal();
            sector.onPlayerLeave(to);
        }
        MapSector previous = sector;
        sector = to;
        sector.onPlayerEnter_internal();
        sector.onPlayerEnter(previous);
    }

    @Override
    public String toString() {
        return id;
    }

}
