package net.egartley.gamelib.objects;

import java.awt.*;
import java.util.ArrayList;

/**
 * One of the game's "states" that has unique render and tick methods
 */
public abstract class GameState {

    /**
     * The ID number used while actually playing the game
     */
    public static final int IN_GAME = 0;

    /**
     * The ID number used while in the main menu (shown on startup)
     */
    public static final int MAIN_MENU = 1;

    /**
     * Collection of "sub", or secondary, game states
     */
    public ArrayList<GameState> subStates;
    /**
     * Unique integer used to identify different game states
     */
    public int identificationNumber;

    public abstract void render(Graphics graphics);

    public abstract void tick();

}