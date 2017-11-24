package net.egartley.beyondorigins.objects;

import java.awt.Graphics;

public abstract class StaticEntity extends Entity {

	protected double x;
	protected double y;
	
	public StaticEntity() {
		isAnimated = false;
		isStatic = true;
	}
	
	public abstract void setEntityBoundary();
	
	public abstract void render(Graphics graphics);
	public abstract void tick();

}
