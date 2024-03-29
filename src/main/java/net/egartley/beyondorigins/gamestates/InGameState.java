package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.engine.map.Map;
import net.egartley.beyondorigins.engine.controllers.KeyboardController;
import net.egartley.beyondorigins.engine.input.KeyTyped;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.ui.ActionUI;
import net.egartley.beyondorigins.engine.ui.panel.DialoguePanel;
import net.egartley.beyondorigins.engine.ui.NotificationBanner;
import net.egartley.beyondorigins.data.Items;
import net.egartley.beyondorigins.data.Quests;
import net.egartley.beyondorigins.engine.io.SaveLoad;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Building;
import net.egartley.beyondorigins.ingame.PlayerMenu;
import net.egartley.beyondorigins.ingame.Quest;
import net.egartley.beyondorigins.ingame.maps.debug.DebugMap;
import net.egartley.beyondorigins.ingame.maps.testbattle.TestBattleMap;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class InGameState extends BasicGameState {

    private static boolean isShowingActionUI;
    private static ActionUI actionUI;
    private static final ArrayList<KeyTyped> keyTypeds = new ArrayList<>();
    private static final ArrayList<NotificationBanner> notifications = new ArrayList<>();

    public static boolean canPlay = true;
    public static boolean isDialogueVisible;
    public static boolean isInventoryVisible;
    public static final int ID = 1;
    public static Map map;
    public static Building building;
    public static PlayerMenu playerMenu;
    public static DialoguePanel dialogue;
    public static ArrayList<Map> maps = new ArrayList<>();

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Entities.initialize();
        Items.init();
        Quests.init();
        maps.add(new DebugMap());
        maps.add(new TestBattleMap());
        playerMenu = new PlayerMenu();
        dialogue = new DialoguePanel();
        changeMap(0);
        keyTypeds.add(new KeyTyped(Input.KEY_E) {
            @Override
            public void onType() {
                // only open player menu when not talking to someone
                if (!isDialogueVisible) {
                    isInventoryVisible = !isInventoryVisible;
                }
            }
        });
        keyTypeds.add(new KeyTyped(Input.KEY_SPACE) {
            @Override
            public void onType() {
                dialogue.advance();
            }
        });
        keyTypeds.add(new KeyTyped(Input.KEY_ESCAPE) {
            @Override
            public void onType() {
                if (!isInventoryVisible && !isDialogueVisible) {
                    game.enterState(MainMenuState.ID);
                }
            }
        });
        keyTypeds.add(new KeyTyped(Input.KEY_F3) {
            @Override
            public void onType() {
                Game.debug = !Game.debug;
            }
        });
        keyTypeds.add(new KeyTyped(Input.KEY_O) {
            @Override
            public void onType() {
                SaveLoad.setCurrentID(0);
                SaveLoad.set("test", "teststring");
                SaveLoad.save();
            }
        });
    }

    /**
     * Leaves the player from the current map, and enters them into the new one. All collisions are ended, then cleared.
     *
     * @param i The index of the map to move to
     * @see Map#onPlayerLeave()
     * @see Map#onPlayerEnter()
     */
    public static void changeMap(int i) {
        Collisions.endAndNuke();
        if (map != null) {
            map.onPlayerLeave();
        }
        map = maps.get(i);
        map.onPlayerEnter();
    }

    /**
     * Give the player a quest
     */
    public static void giveQuest(Quest quest, boolean start) {
        playerMenu.questsPanel.addQuest(quest, start);
        pushNotification(new NotificationBanner("New quest added!"));
    }

    /**
     * Remove, or clear, a quest from the player
     */
    public static void removeQuest(Quest quest) {
        playerMenu.questsPanel.removeQuest(quest);
    }

    /**
     * Add a notification to the notification queue
     */
    public static void pushNotification(NotificationBanner notification) {
        notifications.add(notification);
    }

    /**
     * Remove the notification from the queue
     */
    public static void onNotificationFinish(NotificationBanner notification) {
        notifications.remove(notification);
    }

    public static void showActionUI(String text, int keycode) {
        actionUI = new ActionUI(text, keycode);
        actionUI.tick();
        isShowingActionUI = true;
    }

    public static void hideActionUI() {
        isShowingActionUI = false;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        keyTypeds.forEach(KeyboardController::addKeyTyped);
        Entities.PLAYER.onInGameEnter();
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        Entities.PLAYER.onInGameLeave();
        keyTypeds.forEach(KeyboardController::removeKeyTyped);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
        if (Entities.PLAYER.isInBuilding) {
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
            building.currentFloor.render(graphics);
            Entities.PLAYER.render(graphics);
        } else {
            map.render(graphics);
        }
        if (isShowingActionUI) {
            actionUI.render(graphics);
        }
        if (canPlay) {
            if (isInventoryVisible) {
                playerMenu.render(graphics);
            } else if (isDialogueVisible) {
                dialogue.render(graphics);
            }
        }
        if (!notifications.isEmpty()) {
            // treated as a queue, so only the first is rendered (bad?)
            notifications.get(0).render(graphics);
        }
        if (Game.debug) {
            Debug.render(graphics);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (Entities.PLAYER.isInBuilding) {
            building.currentFloor.checkPlayerLimits();
            building.currentFloor.tick();
            Entities.PLAYER.tick();
        } else {
            map.tick();
        }
        if (isShowingActionUI) {
            actionUI.tick();
        }
        if (canPlay) {
            if (isInventoryVisible) {
                playerMenu.tick();
            } else if (isDialogueVisible) {
                dialogue.tick();
            }
        }
        Collisions.tick();
        if (!notifications.isEmpty()) {
            notifications.get(0).tick();
        }
    }

    @Override
    public int getID() {
        return InGameState.ID;
    }

}
