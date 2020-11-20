package net.egartley.beyondorigins.ingame.maps.testbattle.sectors;

import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.abstracts.MapSector;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.WarpPad;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.ingame.maps.testbattle.TestBattleMap;

public class Sector1 extends MapSector {

    public Sector1(Map parent) {
        super(parent, 1);
    }

    @Override
    public void initialize() {
        ((TestBattleMap) (parent)).spawnMonster(200, 252);
        WarpPad pad = new WarpPad(Entities.getSpriteTemplate(Entities.TEMPLATE_WP), 600, 450);
        pad.collisions.add(new EntityEntityCollision(Entities.PLAYER.boundary, pad.defaultBoundary) {
            public void start(EntityEntityCollisionEvent e) {
                InGameState.changeMap(0);
                end();
            }
        });
        addEntity(pad);
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        Entities.PLAYER.setPosition(300, 300);
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
