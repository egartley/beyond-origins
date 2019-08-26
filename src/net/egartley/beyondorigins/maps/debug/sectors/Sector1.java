package net.egartley.beyondorigins.maps.debug.sectors;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.DefaultRock;
import net.egartley.beyondorigins.entities.DefaultTree;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.objects.Entity;
import net.egartley.gamelib.objects.Map;
import net.egartley.gamelib.objects.MapSector;
import net.egartley.gamelib.objects.MapSectorDefinition;

import java.awt.*;

public class Sector1 extends MapSector {

    public Sector1(Map parent, MapSectorDefinition def) {
        super(parent, def);
    }

    @Override
    public void render(Graphics graphics) {
        drawTiles(graphics);
        for (Entity e : entities) {
            if (e.isDualRendered) {
                e.drawFirstLayer(graphics);
            } else {
                e.render(graphics);
            }
        }
        Entities.DUMMY.render(graphics);
        Entities.PLAYER.render(graphics);
        for (Entity e : entities) {
            if (e.isDualRendered) {
                e.drawSecondLayer(graphics);
            }
        }

        if (Game.debug) {
            changeBoundaries.forEach(boundary -> boundary.draw(graphics));
        }
    }

    @Override
    public void tick() {
        super.tick();

        Entities.DUMMY.tick();
    }

    @Override
    public void initialize() {
        if (!didInitialize) {
            // sector-specific entities
            Sprite s = Entities.getSpriteTemplate(Entities.TEMPLATE_TREE);
            entities.add(new DefaultTree(s, 100, 200));
            entities.add(new DefaultTree(s, 36, 200));
            s = Entities.getSpriteTemplate(Entities.TEMPLATE_ROCK);
            int off = 0;
            for (byte i = 0; i < 14; i++) {
                entities.add(new DefaultRock(s, (s.width * 2) * off++ + 48, 400));
            }
            didInitialize = true;
        }
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        if (from == null) {
            Entities.PLAYER.x(700);
            Entities.PLAYER.y(140);
        } else {
            updatePlayerPosition(from);
        }
        initialize();
        Entities.DUMMY.onSectorEnter(this);
        Entities.PLAYER.onSectorEnter(this);
    }

    @Override
    public void onPlayerLeave(MapSector to) {
        Entities.DUMMY.onSectorLeave(this);
        Entities.PLAYER.onSectorLeave(this);
    }

}
