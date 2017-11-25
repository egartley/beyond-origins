package net.egartley.beyondorigins.logic.interaction;

import java.awt.Color;
import java.awt.Graphics;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.objects.Entity;

public class EntityBoundary extends Boundary {

	public Entity entity;
	public Color drawColor;

	public EntityBoundary(Entity e, int w, int h, int p) {
		entity = e;
		width = w + (2 * p);
		height = h + (2 * p);
		padding = p;
		x = entity.x - padding;
		y = entity.y - padding;
		north = y;
		south = north + height;
		west = x;
		east = west + width;
		setColor();
	}

	public EntityBoundary(Entity e, int w, int h, int p, int x, int y) {
		entity = e;
		width = w + (2 * p);
		height = h + (2 * p);
		padding = p;
		this.x = x - p;
		this.y = y - p;
		north = this.y;
		south = north + height;
		west = this.x;
		east = west + width;
		setColor();
	}

	private void setColor() {
		if (entity.isStatic) {
			drawColor = Color.BLACK;
		} else {
			drawColor = Color.YELLOW;
		}
	}

	@Override
	public void draw(Graphics graphics) {
		if (Game.drawBoundaries) {
			Color previous = graphics.getColor();
			graphics.setColor(drawColor);
			graphics.drawRect(x, y, width, height);
			graphics.setColor(previous);
		}
	}

	@Override
	public void tick() {
		x = entity.x - padding;
		y = entity.y - padding;
		north = y;
		south = north + height;
		west = x;
		east = west + width;
	}

}
