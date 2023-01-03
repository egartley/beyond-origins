package net.egartley.beyondorigins.ingame.maps.testbattle.sectors;

import net.egartley.beyondorigins.engine.map.Map;
import net.egartley.beyondorigins.engine.map.MapSector;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.engine.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Monster;
import net.egartley.beyondorigins.entities.WarpPad;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.ingame.maps.testbattle.TestBattleMap;

public class Sector1 extends MapSector {

    public Sector1(Map parent) {
        super(parent, 1);
    }

    @Override
    public void init() {
        Monster m = new Monster();
        m.setPosition(200, 252);
        addEntityDirect(m);
        WarpPad pad = new WarpPad(Entities.getSpriteTemplate(Entities.TEMPLATE_WP), 600, 450);
        Collisions.add(new EntityEntityCollision(Entities.PLAYER.boundary, pad.defaultBoundary) {
            public void start(EntityEntityCollisionEvent e) {
                InGameState.changeMap(0);
                end();
            }
        });
        addEntityDirect(pad);
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        if (from == null) {
            Entities.PLAYER.setPosition(300, 300);
        } else {
            updatePlayerPosition(from);
        }
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

    @Override
    public void setSpecialCollisions() {

    }

}
