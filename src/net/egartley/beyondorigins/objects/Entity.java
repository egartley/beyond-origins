package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Entity {

	public ArrayList<Sprite> sprites;
	public Sprite currentSprite;
	public int absoluteX, absoluteY;
	public boolean isAnimated;
	public boolean isSingleFrame;
	
	public abstract void render(Graphics graphics);
	public abstract void tick();

	public void setCurrentSprite(int index) {
		currentSprite = sprites.get(index);
	}

}
