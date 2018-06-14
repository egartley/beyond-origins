package net.egartley.beyondorigins.maps.testmap.sectors;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.MapSectorChangeCollision;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeBoundary;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;

import java.awt.*;

public class Sector3 extends MapSector {

    public Sector3(Map parent, MapSectorDefinition def) {
        super(parent, def, new MapSectorChangeBoundary(0, 0, 18, Game.WINDOW_HEIGHT));
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        Entities.PLAYER.render(graphics);

        for (MapSectorChangeBoundary boundary : changeBoundaries) {
            boundary.draw(graphics);
        }
    }

    @Override
    public void tick() {
        Entities.PLAYER.tick();

        for (MapSectorChangeCollision collision : changeCollisions) {
            collision.tick();
        }
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        Entities.PLAYER.x = 52;
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
