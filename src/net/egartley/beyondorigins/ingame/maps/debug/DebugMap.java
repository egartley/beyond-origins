package net.egartley.beyondorigins.ingame.maps.debug;

import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.abstracts.MapSector;
import net.egartley.beyondorigins.core.logic.events.MapSectorChangeEvent;
import net.egartley.beyondorigins.ingame.maps.debug.sectors.Sector1;
import net.egartley.beyondorigins.ingame.maps.debug.sectors.Sector2;
import net.egartley.beyondorigins.ingame.maps.debug.sectors.Sector3;
import net.egartley.beyondorigins.ingame.maps.debug.sectors.Sector4;
import org.newdawn.slick.Graphics;

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

        // stitch together all of the sectors
        sec1.setNeighborAt(sec2, MapSector.TOP);
        sec2.setNeighborAt(sec3, MapSector.RIGHT);
        sec3.setNeighborAt(sec4, MapSector.BOTTOM);
        sec4.setNeighborAt(sec1, MapSector.LEFT);
    }

    @Override
    public void render(Graphics graphics) {
        sector.render(graphics);
    }

    @Override
    public void onSectorChange(MapSectorChangeEvent event) {

    }

    @Override
    public void tick() {
        sector.tick();
    }
}
