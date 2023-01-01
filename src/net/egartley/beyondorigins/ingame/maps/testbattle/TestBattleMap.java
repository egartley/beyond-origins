package net.egartley.beyondorigins.ingame.maps.testbattle;

import net.egartley.beyondorigins.engine.map.Map;
import net.egartley.beyondorigins.engine.map.MapSector;
import net.egartley.beyondorigins.engine.enums.Direction;
import net.egartley.beyondorigins.engine.logic.Calculate;
import net.egartley.beyondorigins.engine.logic.events.MapSectorChangeEvent;
import net.egartley.beyondorigins.entities.FH;
import net.egartley.beyondorigins.entities.Monster;
import net.egartley.beyondorigins.entities.WindChimes;
import net.egartley.beyondorigins.ingame.maps.testbattle.sectors.Sector1;
import net.egartley.beyondorigins.ingame.maps.testbattle.sectors.Sector2;
import org.newdawn.slick.Graphics;

public class TestBattleMap extends Map {

    public TestBattleMap() {
        super("test-battle");
        sectors.add(new Sector1(this));
        sectors.add(new Sector2(this));
        MapSector sec1 = sectors.get(0);
        MapSector sec2 = sectors.get(1);
        sec1.setNeighborAt(sec2, Direction.RIGHT);
    }

    public void spawnFHBoss() {
        FH fh = new FH();
        fh.setPosition(Calculate.getCenteredX(fh.sprite.width), Calculate.getCenteredY(fh.sprite.height));
        sector.addEntity(fh);
        sector.addEntity(new WindChimes(100, 150));
        sector.addEntity(new WindChimes(100, 450));
        sector.addEntity(new WindChimes(600, 150));
        sector.addEntity(new WindChimes(600, 450));
    }

    public void spawnMonster(int x, int y) {
        Monster monster = new Monster();
        monster.setPosition(x, y);
        sector.addEntity(monster);
    }

    @Override
    public void onPlayerEnter() {
        changeSector(sectors.get(0), null);
    }

    @Override
    public void onPlayerLeave() {
        sector.onPlayerLeave_internal();
    }

    @Override
    public void onSectorChange(MapSectorChangeEvent event) {

    }

    @Override
    public void tick() {
        sector.tick();
    }

    @Override
    public void render(Graphics graphics) {
        sector.render(graphics);
    }
}
