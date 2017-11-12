package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class GameState {

	public static final int IN_GAME = 0;
	
	public ArrayList<SubGameState> subStates;
	public int identificationNumber;
	public abstract void render(Graphics graphics);
	public abstract void tick();
	
}