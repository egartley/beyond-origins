package net.egartley.beyondorigins.maps.debug;

import net.egartley.beyondorigins.definitions.maps.debug.Sectors;
import net.egartley.beyondorigins.maps.debug.sectors.Sector1;
import net.egartley.beyondorigins.maps.debug.sectors.Sector2;
import net.egartley.beyondorigins.maps.debug.sectors.Sector3;
import net.egartley.beyondorigins.maps.debug.sectors.Sector4;
import net.egartley.gamelib.logic.events.MapSectorChangeEvent;
import net.egartley.gamelib.objects.Map;
import net.egartley.gamelib.objects.MapSector;

import java.awt.*;

public class DebugMap extends Map {

    public DebugMap(String id) {
        super(id);
        sectors.add(new Sector1(this, Sectors.sector1));
        sectors.add(new Sector2(this, Sectors.sector2));
        sectors.add(new Sector3(this, Sectors.sector3));
        sectors.add(new Sector4(this, Sectors.sector4));

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
    public void tick() {
        sector.tick();
    }

    @Override
    public void render(Graphics graphics) {
        sector.render(graphics);
    }

    @Override
    public void onSectorChange(MapSectorChangeEvent event) {

    }

}
