package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.core.interfaces.Renderable;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import net.egartley.beyondorigins.core.logic.events.MapSectorChangeEvent;

import java.util.ArrayList;

/**
 * Collection of "sectors" that make up a map, which is like a level or world
 */
public abstract class Map implements Tickable, Renderable {

    public String name;
    public MapSector sector;
    public ArrayList<MapSector> sectors = new ArrayList<>();

    public Map(String name) {
        this.name = name;
    }

    public abstract void onPlayerEnter();

    public abstract void onPlayerLeave();

    public abstract void onSectorChange(MapSectorChangeEvent event);

    public void changeSector(MapSector to, MapSector from) {
        if (sector != null) {
            sector.onPlayerLeave_internal();
            sector.onPlayerLeave(to);
        }
        onSectorChange(new MapSectorChangeEvent(from, to));
        MapSector previous = sector;
        sector = to;
        sector.onPlayerEnter_internal();
        sector.onPlayerEnter(previous);
    }

    @Override
    public String toString() {
        return name;
    }

}
