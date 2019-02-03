package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.maps.testmap.TestMap;
import net.egartley.beyondorigins.objects.GameState;
import net.egartley.beyondorigins.objects.Map;

import java.awt.*;

public class InGameState extends GameState {

    private Map currentMap;

    public InGameState() {
        identificationNumber = GameState.IN_GAME;
        currentMap = new TestMap("TestMap");
    }

    @Override
    public void render(Graphics graphics) {
        currentMap.render(graphics);
        DialogueController.render(graphics);

        Debug.render(graphics);
    }

    @Override
    public void tick() {
        DialogueController.tick();
        currentMap.tick();
    }

}
