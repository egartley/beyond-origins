package net.egartley.beyondorigins.gamestates.ingame;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.controllers.KeyboardController;
import net.egartley.beyondorigins.data.ItemStore;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.ingame.substates.InBuildingState;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.beyondorigins.ingame.PlayerMenu;
import net.egartley.beyondorigins.ingame.maps.debug.DebugMap;
import net.egartley.beyondorigins.ui.DialoguePanel;
import net.egartley.gamelib.abstracts.GameState;
import net.egartley.gamelib.abstracts.Map;
import net.egartley.gamelib.input.KeyTyped;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class InGameState extends GameState {

    private KeyTyped toggleInventory, advanceDialogue, backToMainMenu, itest, itest2;

    public Map map;
    public PlayerMenu playerMenu;
    public DialoguePanel dialogue;
    public ArrayList<Map> maps = new ArrayList<>();

    public boolean isInventoryVisible;
    public boolean isDialogueVisible;

    public InGameState() {
        id = GameState.IN_GAME;

        subStates.add(new InBuildingState());

        ItemStore.init();

        // load map
        maps.add(new DebugMap());
        map = maps.get(0);

        // load inventory
        playerMenu = new PlayerMenu();

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
                if (!isInventoryVisible && !isDialogueVisible) {
                    Game.setState(Game.getState(GameState.MAIN_MENU));
                }
            }
        };
        itest = new KeyTyped(KeyEvent.VK_U) {
            @Override
            public void onType() {
                Entities.PLAYER.inventory.put(ItemStore.WIZARD_HAT, 21);
            }
        };
        itest2 = new KeyTyped(KeyEvent.VK_Y) {
            @Override
            public void onType() {
                Entities.PLAYER.inventory.remove(ItemStore.WIZARD_HAT, 3);
            }
        };
    }

    public void setBuilding(Building building) {
        ((InBuildingState) subStates.get(0)).building = building;
    }

    @Override
    public void onStart() {
        KeyboardController.addKeyTyped(toggleInventory);
        KeyboardController.addKeyTyped(advanceDialogue);
        KeyboardController.addKeyTyped(backToMainMenu);
        KeyboardController.addKeyTyped(itest);
        KeyboardController.addKeyTyped(itest2);

        map.changeSector(map.sectors.get(0), null);
    }

    @Override
    public void onEnd() {
        KeyboardController.removeKeyTyped(toggleInventory);
        KeyboardController.removeKeyTyped(advanceDialogue);
        KeyboardController.removeKeyTyped(backToMainMenu);
        KeyboardController.addKeyTyped(itest);
        KeyboardController.addKeyTyped(itest2);
    }

    @Override
    public void render(Graphics graphics) {
        if (currentSubState != null) {
            currentSubState.render(graphics);
        } else {
            map.render(graphics);
        }
        if (isInventoryVisible) {
            playerMenu.render(graphics);
        } else if (isDialogueVisible) {
            dialogue.render(graphics);
        }
        if (Game.debug) {
            Debug.render(graphics);
        }
    }

    @Override
    public void tick() {
        // normal gamestate tick
        if (currentSubState != null) {
            currentSubState.tick();
        } else {
            map.tick();
        }

        // tick regardless of gamestate
        if (isInventoryVisible) {
            playerMenu.tick();
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
