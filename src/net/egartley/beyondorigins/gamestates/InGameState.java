package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.controllers.KeyboardController;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.Inventory;
import net.egartley.beyondorigins.maps.debug.DebugMap;
import net.egartley.gamelib.input.KeyTyped;
import net.egartley.gamelib.objects.GameState;
import net.egartley.gamelib.objects.Map;

import java.awt.*;
import java.awt.event.KeyEvent;

public class InGameState extends GameState {

    private Map currentMap;
    private KeyTyped toggleInventory, advanceDialogue, backToMainMenu;
    public Inventory inventory;

    public boolean isInventoryVisible;
    public boolean isDialogueVisible;

    public InGameState() {
        identificationNumber = GameState.IN_GAME;

        // load map
        currentMap = new DebugMap("Debug Map");

        // load inventory
        inventory = new Inventory(Entities.getTemplate(Entities.TEMPLATE_INVENTORY));

        // initialize key typeds
        toggleInventory = new KeyTyped(KeyEvent.VK_E) {
            @Override
            public void onType() {
                if (!isDialogueVisible) {
                    isInventoryVisible = !isInventoryVisible;
                }
            }
        };
        advanceDialogue = new KeyTyped(KeyEvent.VK_SPACE) {
            @Override
            public void onType() {
                Entities.DIALOGUE_PANEL.advance();
            }
        };
        backToMainMenu = new KeyTyped(KeyEvent.VK_ESCAPE) {
            @Override
            public void onType() {
                Game.setState(Game.getState(GameState.MAIN_MENU));
            }
        };
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    @Override
    public void onStart() {
        KeyboardController.addKeyTyped(toggleInventory);
        KeyboardController.addKeyTyped(advanceDialogue);
        KeyboardController.addKeyTyped(backToMainMenu);
    }

    @Override
    public void onEnd() {
        KeyboardController.removeKeyTyped(toggleInventory);
        KeyboardController.removeKeyTyped(advanceDialogue);
        KeyboardController.removeKeyTyped(backToMainMenu);
    }

    @Override
    public void render(Graphics graphics) {
        currentMap.render(graphics);
        if (isDialogueVisible) {
            Entities.DIALOGUE_PANEL.render(graphics);
        } else if (isInventoryVisible) {
            inventory.render(graphics);
        }
        Debug.render(graphics);
    }

    @Override
    public void tick() {
        currentMap.tick();
        if (isDialogueVisible) {
            Entities.DIALOGUE_PANEL.tick();
        } else if (isInventoryVisible) {
            inventory.tick();
        }
    }

}
