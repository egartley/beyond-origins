package net.egartley.beyondorigins.maps.testmap;

import net.egartley.beyondorigins.logic.events.MapSectorChangeEvent;
import net.egartley.beyondorigins.maps.testmap.sectors.Sector1;
import net.egartley.beyondorigins.maps.testmap.sectors.Sector2;
import net.egartley.beyondorigins.maps.testmap.sectors.Sector3;
import net.egartley.beyondorigins.objects.Map;

import java.awt.*;

public class TestMap extends Map {

    public TestMap(String id) {
        super(id);
        sectors.add(new Sector1(this, net.egartley.beyondorigins.definitions.maps.testmap.Sectors.sector1));
        sectors.add(new Sector2(this, net.egartley.beyondorigins.definitions.maps.testmap.Sectors.sector2));
        sectors.add(new Sector3(this, net.egartley.beyondorigins.definitions.maps.testmap.Sectors.sector3));
        // go to the first sector by default
        changeSector(sectors.get(0), null);
        // stupid fix for sector changing, should be implemented in the map sector
        // change boundary constructor somehow
        sectors.get(0).changeBoundaries.get(0).to = sectors.get(1);
        sectors.get(1).changeBoundaries.get(0).to = sectors.get(0);
        sectors.get(1).changeBoundaries.get(1).to = sectors.get(2);
        sectors.get(2).changeBoundaries.get(0).to = sectors.get(1);
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
