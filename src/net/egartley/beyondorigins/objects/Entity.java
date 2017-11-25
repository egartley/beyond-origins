package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.logic.interaction.EntityBoundary;

public abstract class Entity {

	public ArrayList<Sprite> spriteCollection;
	public Sprite currentSprite;
	public EntityBoundary boundary;
	public int x, y;
	public boolean isAnimated, isStatic, isCollided;
	
	public abstract void render(Graphics graphics);
	public abstract void tick();
	public abstract void setBoundary();
	
	public void setCurrentSprite(int index) {
		currentSprite = spriteCollection.get(index);
	}

}
