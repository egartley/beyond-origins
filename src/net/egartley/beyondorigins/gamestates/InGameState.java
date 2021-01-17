package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.abstracts.Map;
import net.egartley.beyondorigins.core.controllers.KeyboardController;
import net.egartley.beyondorigins.core.input.KeyTyped;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.ui.DialoguePanel;
import net.egartley.beyondorigins.core.ui.NotificationBanner;
import net.egartley.beyondorigins.data.Items;
import net.egartley.beyondorigins.data.Quests;
import net.egartley.beyondorigins.data.SaveLoad;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.beyondorigins.ingame.PlayerMenu;
import net.egartley.beyondorigins.ingame.Quest;
import net.egartley.beyondorigins.ingame.maps.debug.DebugMap;
import net.egartley.beyondorigins.ingame.maps.testbattle.TestBattleMap;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class InGameState extends BasicGameState {

    private static final ArrayList<KeyTyped> keyTypeds = new ArrayList<>();
    private static final ArrayList<NotificationBanner> notifications = new ArrayList<>();

    public static Map map;
    public static Building building;
    public static PlayerMenu playerMenu;
    public static DialoguePanel dialogue;
    public static ArrayList<Map> maps = new ArrayList<>();
    public static boolean canPlay = true;
    public static boolean isDialogueVisible;
    public static boolean isInventoryVisible;
    public static final int ID = 1;

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
        for (EntityEntityCollision collision : Collisions.with(Entities.PLAYER)) {
            collision.end();
        }
        Collisions.nuke();
        if (map != null) {
            map.onPlayerLeave();
        }
        map = maps.get(i);
        map.onPlayerEnter();
    }

    /**
     * Give the player a quest
     *
     * @param quest The quest to give the player
     * @param start Whether or not to "start" the quest after giving it
     */
    public static void giveQuest(Quest quest, boolean start) {
        playerMenu.questsPanel.add(quest, start);
        pushNotification(new NotificationBanner("New quest added!"));
    }

    /**
     * Remove, or clear, a quest from the player
     *
     * @param quest The quest to remove
     */
    public static void removeQuest(Quest quest) {
        playerMenu.questsPanel.remove(quest);
    }

    /**
     * Add a notification to the notification queue
     *
     * @param notification The notification to add
     * @see #notifications
     */
    public static void pushNotification(NotificationBanner notification) {
        notifications.add(notification);
    }

    /**
     * Remove the notification from the queue
     *
     * @param notification The notification to remove
     * @see #notifications
     */
    public static void onNotificationFinish(NotificationBanner notification) {
        notifications.remove(notification);
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
        // first, check if we're in a building
        if (Entities.PLAYER.isInBuilding) {
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
            building.currentFloor.render(graphics);
            Entities.PLAYER.render(graphics);
        } else {
            // not in a building, so just render the map as normal
            map.render(graphics);
        }
        // check if the user can interact at all
        if (canPlay) {
            if (isInventoryVisible) {
                playerMenu.render(graphics);
            } else if (isDialogueVisible) {
                dialogue.render(graphics);
            }
        }
        // check for any pending notifications
        if (!notifications.isEmpty()) {
            // treated as a queue, so only the first is rendered
            notifications.get(0).render(graphics);
        }
        if (Game.debug) {
            Debug.render(graphics);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // see render method for comments
        if (Entities.PLAYER.isInBuilding) {
            building.currentFloor.checkPlayerLimits();
            building.currentFloor.tick();
            Entities.PLAYER.tick();
        } else {
            map.tick();
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
