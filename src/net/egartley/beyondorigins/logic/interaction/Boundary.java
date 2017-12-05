package net.egartley.beyondorigins.logic.interaction;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Boundary {

	public int x, y, width, height, padding, top, right, bottom, left;

	public abstract void draw(Graphics graphics);

	public abstract void tick();

	public Rectangle asRectangle()
	{
		return new Rectangle(x, y, width, height);
	}

}
