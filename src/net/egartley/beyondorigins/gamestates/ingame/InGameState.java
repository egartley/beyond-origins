package net.egartley.beyondorigins.gamestates.ingame;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.controllers.KeyboardController;
import net.egartley.beyondorigins.gamestates.ingame.substates.InBuildingState;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.beyondorigins.ingame.Inventory;
import net.egartley.beyondorigins.maps.debug.DebugMap;
import net.egartley.beyondorigins.ui.DialoguePanel;
import net.egartley.gamelib.abstracts.GameState;
import net.egartley.gamelib.abstracts.Map;
import net.egartley.gamelib.input.KeyTyped;

import java.awt.*;
import java.awt.event.KeyEvent;

public class InGameState extends GameState {

    private KeyTyped toggleInventory, advanceDialogue, backToMainMenu;

    public Map map;
    public Inventory inventory;
    public DialoguePanel dialogue;

    public boolean isInventoryVisible;
    public boolean isDialogueVisible;

    public InGameState() {
        id = GameState.IN_GAME;

        subStates.add(new InBuildingState());

        // load map
        map = new DebugMap("Debug Map");

        // load inventory
        inventory = new Inventory();

        // load dialogue panel
        dialogue = new DialoguePanel();

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
                dialogue.advance();
            }
        };
        backToMainMenu = new KeyTyped(KeyEvent.VK_ESCAPE) {
            @Override
            public void onType() {
                Game.setState(Game.getState(GameState.MAIN_MENU));
            }
        };
    }

    private void tick_this() {
        map.tick();
        if (isInventoryVisible) {
            inventory.tick();
        }
    }

    private void render_this(Graphics graphics) {
        map.render(graphics);
    }

    public void setBuilding(Building building) {
        ((InBuildingState) subStates.get(0)).building = building;
    }

    @Override
    public void onStart() {
        KeyboardController.addKeyTyped(toggleInventory);
        KeyboardController.addKeyTyped(advanceDialogue);
        KeyboardController.addKeyTyped(backToMainMenu);

        map.changeSector(map.sectors.get(0), null);
    }

    @Override
    public void onEnd() {
        KeyboardController.removeKeyTyped(toggleInventory);
        KeyboardController.removeKeyTyped(advanceDialogue);
        KeyboardController.removeKeyTyped(backToMainMenu);
    }

    @Override
    public void render(Graphics graphics) {
        if (currentSubState != null) {
            currentSubState.render(graphics);
        } else {
            render_this(graphics);
        }
        if (isInventoryVisible) {
            inventory.render(graphics);
        } else if (isDialogueVisible) {
            dialogue.render(graphics);
        }
        if (Game.debug) {
            Debug.render(graphics);
        }
    }

    @Override
    public void tick() {
        // normal gmaestate tick
        if (currentSubState != null) {
            currentSubState.tick();
        } else {
            tick_this();
        }

        // tick regardless of gamestate
        if (isInventoryVisible) {
            inventory.tick();
        } else if (isDialogueVisible) {
            dialogue.tick();
        }
    }

    @Override
    public boolean hasSubStates() {
        return true;
    }

    @Override
    public boolean isSubState() {
        return false;
    }

}
