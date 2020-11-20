package net.egartley.beyondorigins.ingame.maps.testbattle;

import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.logic.events.MapSectorChangeEvent;
import net.egartley.beyondorigins.entities.Monster;
import net.egartley.beyondorigins.ingame.maps.testbattle.sectors.Sector1;
import org.newdawn.slick.Graphics;

public class TestBattleMap extends Map {

    public TestBattleMap() {
        super("test-battle");
        sectors.add(new Sector1(this));
    }

    public void spawnMonster(int x, int y) {
        Monster monster = new Monster();
        monster.setPosition(x, y);
        sector.addEntity(monster);
    }

    @Override
    public void render(Graphics graphics) {
        sector.render(graphics);
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
}
