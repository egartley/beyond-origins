package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * One of the game's "states" that has unique render and tick methods
 * 
 * @author Evan Gartley
 * @see SubGameState
 */
public abstract class GameState {

	/**
	 * The ID number used while actually playing the game
	 * 
	 * @see GameState
	 */
	public static final int			IN_GAME	= 0;

	/**
	 * Collection of possible "sub", or lower level, game states
	 * 
	 * @see SubGameState
	 */
	public ArrayList<SubGameState>	subStates;
	/**
	 * Unique integer used to identify different game states
	 */
	public int						identificationNumber;

	public abstract void render(Graphics graphics);

	public abstract void tick();

}
