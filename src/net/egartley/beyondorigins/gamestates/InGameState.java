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

    public static final int ID = 1;

    private static final ArrayList<KeyTyped> keyTypeds = new ArrayList<>();
    private static final ArrayList<NotificationBanner> notifications = new ArrayList<>();

    public static Map map;
    public static PlayerMenu playerMenu;
    public static DialoguePanel dialogue;
    public static Building building;
    public static ArrayList<Map> maps = new ArrayList<>();

    public static boolean isInventoryVisible;
    public static boolean isDialogueVisible;
    public static boolean canPlay = true;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
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

    public static void giveQuest(Quest quest, boolean start) {
        playerMenu.questsPanel.add(quest, start);
        pushNotification(new NotificationBanner("New quest added!"));
    }

    public static void removeQuest(Quest quest) {
        playerMenu.questsPanel.remove(quest);
    }

    public static void pushNotification(NotificationBanner notification) {
        notifications.add(notification);
    }

    public static void onNotificationFinish(NotificationBanner notification) {
        notifications.remove(notification);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        for (KeyTyped kt : keyTypeds) {
            KeyboardController.addKeyTyped(kt);
        }
        Entities.PLAYER.onInGameEnter();
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        Entities.PLAYER.onInGameLeave();
        for (KeyTyped kt : keyTypeds) {
            KeyboardController.removeKeyTyped(kt);
        }
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
        if (canPlay) {
            if (isInventoryVisible) {
                playerMenu.render(graphics);
            } else if (isDialogueVisible) {
                dialogue.render(graphics);
            }
        }
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
            // treated as a queue, so only the first is ticked
            notifications.get(0).tick();
        }
    }

    @Override
    public int getID() {
        return InGameState.ID;
    }

}
