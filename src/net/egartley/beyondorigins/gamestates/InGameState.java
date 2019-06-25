package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.controllers.KeyboardController;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.DialoguePanel;
import net.egartley.beyondorigins.ingame.Inventory;
import net.egartley.beyondorigins.maps.debug.DebugMap;
import net.egartley.gamelib.input.KeyTyped;
import net.egartley.gamelib.objects.GameState;
import net.egartley.gamelib.objects.Map;

import java.awt.*;
import java.awt.event.KeyEvent;

public class InGameState extends GameState {

    private Map currentMap;
    public Inventory inventory;

    public boolean isInventoryVisible;

    public InGameState() {
        identificationNumber = GameState.IN_GAME;

        // load map
        currentMap = new DebugMap("Debug Map");

        // load inventory
        inventory = new Inventory(Entities.getTemplate(Entities.TEMPLATE_INVENTORY));

        // load dialogue panel
        Entities.DIALOGUE_PANEL = new DialoguePanel(Entities.getTemplate(Entities.TEMPLATE_DIALOGUE));

        // add key typeds
        KeyboardController.addKeyTyped(new KeyTyped(KeyEvent.VK_E) {
            @Override
            public void onType() {
                isInventoryVisible = !isInventoryVisible;
            }
        });
        KeyboardController.addKeyTyped(new KeyTyped(KeyEvent.VK_SPACE) {
            @Override
            public void onType() {
                Entities.DIALOGUE_PANEL.advance();
            }
        });
    }

    @Override
    public void render(Graphics graphics) {
        currentMap.render(graphics);
        DialogueController.render(graphics);
        if (isInventoryVisible) {
            inventory.render(graphics);
        }

        Debug.render(graphics);
    }

    @Override
    public void tick() {
        currentMap.tick();
        DialogueController.tick();
        if (isInventoryVisible) {
            inventory.tick();
        }
    }

}
