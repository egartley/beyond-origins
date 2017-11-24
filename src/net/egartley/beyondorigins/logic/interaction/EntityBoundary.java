package net.egartley.beyondorigins.logic.interaction;

import java.awt.Graphics;

import net.egartley.beyondorigins.objects.Entity;

public class EntityBoundary extends Boundary {

	public Entity entity;
	
	public EntityBoundary(Entity e) {
		entity = e;
	}
	
	public EntityBoundary(Entity e, int w, int h) {
		entity = e;
		width = w;
		height = h;
	}
	
	public EntityBoundary(Entity e, int w, int h, double x, double y) {
		entity = e;
		width = w;
		height = h;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void draw(Graphics graphics) {
		graphics.drawRect((int) x, (int) y, width, height);
	}

	@Override
	public void tick() {
		x = entity.absoluteX;
		y = entity.absoluteY;
	}

}
