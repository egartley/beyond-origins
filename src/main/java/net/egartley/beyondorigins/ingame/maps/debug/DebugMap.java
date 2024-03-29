package net.egartley.beyondorigins.ingame.maps.debug;

import net.egartley.beyondorigins.engine.map.Map;
import net.egartley.beyondorigins.engine.map.MapSector;
import net.egartley.beyondorigins.engine.enums.Direction;
import net.egartley.beyondorigins.engine.logic.events.MapSectorChangeEvent;
import net.egartley.beyondorigins.ingame.maps.debug.sectors.Sector1;
import net.egartley.beyondorigins.ingame.maps.debug.sectors.Sector2;
import net.egartley.beyondorigins.ingame.maps.debug.sectors.Sector3;
import net.egartley.beyondorigins.ingame.maps.debug.sectors.Sector4;
import org.newdawn.slick.Graphics;

/**
 * Test map
 */
public class DebugMap extends Map {

    public DebugMap() {
        super("debug");
        sectors.add(new Sector1(this));
        sectors.add(new Sector2(this));
        sectors.add(new Sector3(this));
        sectors.add(new Sector4(this));
        MapSector sec1 = sectors.get(0);
        MapSector sec2 = sectors.get(1);
        MapSector sec3 = sectors.get(2);
        MapSector sec4 = sectors.get(3);
        sec1.setNeighborAt(sec2, Direction.UP);
        sec2.setNeighborAt(sec3, Direction.RIGHT);
        sec3.setNeighborAt(sec4, Direction.DOWN);
        sec4.setNeighborAt(sec1, Direction.LEFT);
    }

    @Override
    public void render(Graphics graphics) {
        sector.render(graphics);
    }

    @Override
    public void onPlayerEnter() {
        changeSector(sectors.get(0), null);
    }

    @Override
    public void onPlayerLeave() {
        sector.onPlayerLeave_internal();
        sector.onPlayerLeave(null);
    }

    @Override
    public void onSectorChange(MapSectorChangeEvent event) {

    }

    @Override
    public void tick() {
        sector.tick();
    }
}
