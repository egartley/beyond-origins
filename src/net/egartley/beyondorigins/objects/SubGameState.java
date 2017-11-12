package net.egartley.beyondorigins.objects;

import java.awt.Graphics;

public abstract class SubGameState extends GameState {

	public int identificationNumber;
	@Override
	public abstract void render(Graphics graphics);
	@Override
	public abstract void tick();

}
