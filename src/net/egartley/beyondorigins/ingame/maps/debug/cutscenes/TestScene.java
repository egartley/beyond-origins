package net.egartley.beyondorigins.ingame.maps.debug.cutscenes;

import net.egartley.beyondorigins.core.abstracts.Cutscene;
import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.abstracts.MapSector;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.InGameState;

/**
 * Test cutscene
 */
public class TestScene extends Cutscene {

    public TestScene(Map parent) {
        super(parent, 90);
    }

    @Override
    public void init() {

    }

    @Override
    public void onPlayerEnter(MapSector from) {
        InGameState.canPlay = false;
        Entities.PLAYER.onSectorEnter(this);
        Entities.DUMMY.onSectorEnter(this);
    }

    @Override
    public void onPlayerLeave(MapSector to) {
        InGameState.canPlay = true;
        Entities.DUMMY.onSectorLeave(this);
        Entities.PLAYER.onSectorLeave(this);
    }

    @Override
    public void setSpecialCollisions() {

    }

}
