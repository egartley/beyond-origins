package net.egartley.beyondorigins.ingame.maps.testbattle.sectors;

import net.egartley.beyondorigins.engine.logic.Calculate;
import net.egartley.beyondorigins.engine.map.Map;
import net.egartley.beyondorigins.engine.map.MapSector;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.FH;
import net.egartley.beyondorigins.entities.WindChimes;
import net.egartley.beyondorigins.ingame.maps.testbattle.TestBattleMap;

public class Sector2 extends MapSector {

    public Sector2(Map parent) {
        super(parent, 2);
    }

    @Override
    public void init() {

    }

    @Override
    public void setSpecialCollisions() {

    }

    @Override
    public void onPlayerEnter(MapSector from) {
        FH fh = new FH();
        fh.setPosition(Calculate.getCenteredX(fh.sprite.width), Calculate.getCenteredY(fh.sprite.height));
        addEntityDirect(fh);
        addEntityDirect(new WindChimes(100, 150));
        addEntityDirect(new WindChimes(100, 450));
        addEntityDirect(new WindChimes(600, 150));
        addEntityDirect(new WindChimes(600, 450));
        updatePlayerPosition(from);
        Entities.PLAYER.generateSectorSpecificCollisions(this, true);
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
