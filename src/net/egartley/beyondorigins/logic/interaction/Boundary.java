package net.egartley.beyondorigins.logic.interaction;

import java.awt.Graphics;

public abstract class Boundary {
	
	public double x, y;
	public int width, height;
	public abstract void draw(Graphics graphics);
	public abstract void tick();
	
}
