package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.objects.Entity;
import net.egartley.beyondorigins.objects.Sprite;

public class Player extends Entity {
	
	public Player(ArrayList<Sprite> sprites) {
		this.sprites = sprites;
		currentSprite = sprites.get(0);
		setOtherValues();
	}
	
	public Player(ArrayList<Sprite> sprites, Sprite current) {
		this.sprites = sprites;
		currentSprite = current;
		setOtherValues();
	}
	
	private void setOtherValues() {
		isAnimated = true;
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(currentSprite.getCurrentFrameAsBufferedImage(), absoluteX, absoluteY, null);
	}

	@Override
	public void tick() {
		// currentSprite =	sprites.get(1);
		// currentSprite.currentFrame = 0;
	}

}
