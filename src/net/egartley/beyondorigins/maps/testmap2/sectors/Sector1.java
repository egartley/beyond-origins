package net.egartley.beyondorigins.maps.testmap2.sectors;

import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;

import java.awt.*;

public class Sector1 extends MapSector {

    public Sector1(Map parent, MapSectorDefinition def) {
        super(parent, def);
        definition = def;
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void tick() {

    }

    @Override
    public void onPlayerEnter(MapSector from) {

    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
