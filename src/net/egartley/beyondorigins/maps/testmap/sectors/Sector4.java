package net.egartley.beyondorigins.maps.testmap.sectors;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.MapSectorChangeCollision;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeBoundary;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;

import java.awt.*;

public class Sector4 extends MapSector {

    public Sector4(Map parent, MapSectorDefinition def) {
        super(parent, def);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        Entities.PLAYER.render(graphics);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        if (from.equals(parent.sectors.get(2))) {
            // from sector 3
            Entities.PLAYER.y = 50;
        } else if (from.equals(parent.sectors.get(0))) {
            // from sector 1
            Entities.PLAYER.x = 84;
        }
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
