package net.egartley.beyondorigins.logic.interaction;

import java.awt.Graphics;

public abstract class Boundary {
	
	public int x, y, width, height, padding, north, east, south, west;
	public abstract void draw(Graphics graphics);
	public abstract void tick();
	
}
