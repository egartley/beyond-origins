package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.controllers.KeyboardController;
import net.egartley.beyondorigins.core.input.KeyTyped;
import net.egartley.beyondorigins.core.ui.DialoguePanel;
import net.egartley.beyondorigins.core.ui.QuestsPanel;
import net.egartley.beyondorigins.data.Items;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.beyondorigins.ingame.PlayerMenu;
import net.egartley.beyondorigins.ingame.maps.debug.DebugMap;
import net.egartley.beyondorigins.ingame.maps.testbattle.TestBattleMap;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class InGameState extends BasicGameState {

    public static final int ID = 1;

    private KeyTyped toggleInventory;
    private KeyTyped advanceDialogue;
    private KeyTyped backToMainMenu;
    private KeyTyped toggleDebug;


    public static Map map;
    public static PlayerMenu playerMenu;
    public static QuestsPanel quests;
    public static DialoguePanel dialogue;
    public static Building building;
    public static ArrayList<Map> maps = new ArrayList<>();

    public static boolean isInventoryVisible;
    public static boolean isDialogueVisible;

    public InGameState() {

    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Items.init();

        maps.add(new DebugMap());
        maps.add(new TestBattleMap());
        playerMenu = new PlayerMenu();
        dialogue = new DialoguePanel();
        quests = playerMenu.questsPanel;
        changeMap(0);

        toggleInventory = new KeyTyped(Input.KEY_E) {
            @Override
            public void onType() {
                if (!isDialogueVisible) {
                    isInventoryVisible = !isInventoryVisible;
                }
            }
        };
        advanceDialogue = new KeyTyped(Input.KEY_SPACE) {
            @Override
            public void onType() {
                dialogue.advance();
            }
        };
        backToMainMenu = new KeyTyped(Input.KEY_ESCAPE) {
            @Override
            public void onType() {
                if (!isInventoryVisible && !isDialogueVisible) {
                    game.enterState(MainMenuState.ID);
                }
            }
        };
        toggleDebug = new KeyTyped(Input.KEY_F3) {
            @Override
            public void onType() {
                Game.debug = !Game.debug;
            }
        };
    }

    public static void changeMap(int i) {
        map = maps.get(i);
        map.onMapChange();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        KeyboardController.addKeyTyped(toggleInventory);
        KeyboardController.addKeyTyped(advanceDialogue);
        KeyboardController.addKeyTyped(backToMainMenu);
        KeyboardController.addKeyTyped(toggleDebug);
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        KeyboardController.removeKeyTyped(toggleInventory);
        KeyboardController.removeKeyTyped(advanceDialogue);
        KeyboardController.removeKeyTyped(backToMainMenu);
        KeyboardController.removeKeyTyped(toggleDebug);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
        if (Entities.PLAYER.isInBuilding) {
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);
            building.currentFloor.render(graphics);
            Entities.PLAYER.render(graphics);
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
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // normal gamestate tick
        if (Entities.PLAYER.isInBuilding) {
            building.currentFloor.checkPlayerLimits();
            building.currentFloor.tick();
            Entities.PLAYER.tick();
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
    public int getID() {
        return InGameState.ID;
    }

}
