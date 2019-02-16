package net.egartley.beyondorigins.maps.debug;

import net.egartley.beyondorigins.definitions.maps.debug.Sectors;
import net.egartley.beyondorigins.logic.events.MapSectorChangeEvent;
import net.egartley.beyondorigins.maps.debug.sectors.Sector1;
import net.egartley.beyondorigins.maps.debug.sectors.Sector2;
import net.egartley.beyondorigins.maps.debug.sectors.Sector3;
import net.egartley.beyondorigins.maps.debug.sectors.Sector4;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;

import java.awt.*;

public class DebugMap extends Map {

    public DebugMap(String id) {
        super(id);
        sectors.add(new Sector1(this, Sectors.blankGrass));
        sectors.add(new Sector2(this, Sectors.blankGrass));
        sectors.add(new Sector3(this, Sectors.blankGrass));
        sectors.add(new Sector4(this, Sectors.blankGrass));

        MapSector sec1 = sectors.get(0);
        MapSector sec2 = sectors.get(1);
        MapSector sec3 = sectors.get(2);
        MapSector sec4 = sectors.get(3);

        // go to the first sector by default
        changeSector(sec1, null);

        sec1.setNeighborAt(sec2, MapSector.TOP);
        sec2.setNeighborAt(sec3, MapSector.RIGHT);
        sec3.setNeighborAt(sec4, MapSector.BOTTOM);
        sec4.setNeighborAt(sec1, MapSector.LEFT);
    }

    @Override
    public void tick() {
        currentSector.tick();
    }

    @Override
    public void render(Graphics graphics) {
        currentSector.render(graphics);
    }

    @Override
    public void onSectorChange(MapSectorChangeEvent event) {

    }

}
