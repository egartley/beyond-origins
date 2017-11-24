package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.logic.interaction.EntityBoundary;

public abstract class Entity {

	public ArrayList<Sprite> spriteCollection;
	public Sprite currentSprite;
	public EntityBoundary boundary;
	public double absoluteX, absoluteY;
	public boolean isAnimated;
	public boolean isStatic;
	
	public abstract void render(Graphics graphics);
	public abstract void tick();
	
	public void setCurrentSprite(int index) {
		currentSprite = spriteCollection.get(index);
	}

}
