package net.egartley.gamelib.abstracts;

import net.egartley.gamelib.interfaces.Tickable;

import java.awt.*;
import java.util.ArrayList;

/**
 * One of the game's "states" that has unique render and tick methods
 */
public abstract class GameState implements Tickable {

    /**
     * The ID number used while actually playing the game
     */
    public static final int IN_GAME = 0;
    /**
     * The ID number used while in the main menu (shown on startup)
     */
    public static final int MAIN_MENU = 1;
    /**
     * The ID number used while in game, but also in a building
     */
    public static final int IN_GAME_IN_BUILDING = 2;

    public GameState currentSubState;
    /**
     * Collection of "sub", or secondary, game states
     */
    public ArrayList<GameState> subStates = new ArrayList<>();
    /**
     * Unique integer used to identify different game states
     */
    public int id;

    public void setSubState(GameState subState) {
        if (currentSubState != null) {
            currentSubState.onEnd();
        }
        currentSubState = subState;
        if (currentSubState != null) {
            currentSubState.onStart();
        }
    }

    public abstract void onStart();

    public abstract void onEnd();

    public abstract void render(Graphics graphics);

    public abstract boolean hasSubStates();

    public abstract boolean isSubState();

}
