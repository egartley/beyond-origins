package net.egartley.beyondorigins.gamestates.ingame;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.data.Items;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.beyondorigins.ingame.PlayerMenu;
import net.egartley.beyondorigins.ingame.maps.debug.DebugMap;
import net.egartley.beyondorigins.ui.DialoguePanel;
import net.egartley.beyondorigins.ui.QuestsPanel;
import net.egartley.gamelib.abstracts.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class InGameState extends BasicGameState {

    public static final int ID = 1;

    public ArrayList<Map> maps = new ArrayList<>();

    public static Map map;
    public static PlayerMenu playerMenu;
    public static QuestsPanel quests;
    public static DialoguePanel dialogue;
    public static Building building;

    public static boolean isInventoryVisible;
    public static boolean isDialogueVisible;

    public InGameState(GameContainer container, StateBasedGame game) {
        try {
            init(container, game);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Items.init();

        maps.add(new DebugMap());
        map = maps.get(0);

        playerMenu = new PlayerMenu();
        dialogue = new DialoguePanel();
        quests = playerMenu.questsPanel;

        map.changeSector(map.sectors.get(0), null);

        /* initialize key typeds
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
        };*/
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
        return 0;
    }

}
