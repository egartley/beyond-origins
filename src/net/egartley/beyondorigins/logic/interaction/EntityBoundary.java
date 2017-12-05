package net.egartley.beyondorigins.logic.interaction;

import java.awt.Color;
import java.awt.Graphics;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.objects.Entity;

public class EntityBoundary extends Boundary {

	public Entity	entity;
	public Color	drawColor;

	public EntityBoundary(Entity e, int w, int h, int p) {
		entity = e;
		width = w + (2 * p);
		height = h + (2 * p);
		padding = p;
		x = entity.x - padding;
		y = entity.y - padding;
		top = y;
		bottom = top + height;
		left = x;
		right = left + width;
		setColor();
	}

	public EntityBoundary(Entity e, int w, int h, int p, int x, int y) {
		entity = e;
		width = w + (2 * p);
		height = h + (2 * p);
		padding = p;
		this.x = x - p;
		this.y = y - p;
		top = this.y;
		bottom = top + height;
		left = this.x;
		right = left + width;
		setColor();
	}

	private void setColor()
	{
		if (entity.isStatic) {
			drawColor = Color.BLACK;
		}
		else {
			drawColor = Color.YELLOW;
		}
	}

	@Override
	public void draw(Graphics graphics)
	{
		if (Game.drawBoundaries) {
			Color previous = graphics.getColor();
			graphics.setColor(drawColor);
			graphics.drawRect(x, y, width, height);
			graphics.setColor(previous);
		}
	}

	@Override
	public void tick()
	{
		x = entity.x - padding;
		y = entity.y - padding;
		top = y;
		bottom = top + height;
		left = x;
		right = left + width;
	}

}
