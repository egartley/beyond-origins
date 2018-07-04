package net.egartley.beyondorigins.maps.testmap.sectors;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;

import java.awt.*;

public class Sector2 extends MapSector {

    public Sector2(Map parent, MapSectorDefinition def) {
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
    public void initialize() {

    }

    @Override
    public void onPlayerEnter(MapSector from) {
        if (from.equals(parent.sectors.get(0))) {
            // from sector 1
            Entities.PLAYER.y = Game.WINDOW_HEIGHT - 84;
        } else if (from.equals(parent.sectors.get(2))) {
            // from sector 3
            Entities.PLAYER.x = Game.WINDOW_WIDTH - 84;
        }
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
