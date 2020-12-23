package net.egartley.beyondorigins.ingame.maps.debug.sectors;

import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.abstracts.MapSector;
import net.egartley.beyondorigins.entities.CutsceneTrigger;
import net.egartley.beyondorigins.ingame.maps.debug.cutscenes.TestScene;

public class Sector4 extends MapSector {

    public Sector4(Map parent) {
        super(parent, 4);
    }

    @Override
    public void initialize() {
        addEntity(new CutsceneTrigger(new TestScene(parent), 400, 200));
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        updatePlayerPosition(from);
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

}
