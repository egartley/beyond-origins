package net.egartley.beyondorigins.gamestates.ingame.substates;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.gamelib.abstracts.GameState;

import java.awt.*;

public class InBuildingState extends GameState {

    public Building building;

    public InBuildingState() {
        id = GameState.IN_GAME_IN_BUILDING;
    }

    @Override
    public void onStart() {
        building.onPlayerEnter();
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);
        building.currentFloor.render(graphics);
        Entities.PLAYER.render(graphics);
    }

    @Override
    public void tick() {
        building.currentFloor.checkPlayerLimits();
        building.currentFloor.tick();
        Entities.PLAYER.tick();
    }

    @Override
    public boolean hasSubStates() {
        return false;
    }

    @Override
    public boolean isSubState() {
        return true;
    }

}
