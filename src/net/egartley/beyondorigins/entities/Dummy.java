package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

public class Dummy extends StaticEntity {

	public Dummy(Sprite sprite) {
		currentSprite = sprite;
		x = 470;
		y = 190;
		setEntityBoundary();
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(currentSprite.getCurrentFrameAsBufferedImage(), (int) x, (int) y, null);
		if (Game.drawBoundaries) {
			boundary.draw(graphics);
		}
	}

	@Override
	public void tick() {

	}

	@Override
	public void setEntityBoundary() {
		BufferedImage image = currentSprite.getCurrentFrameAsBufferedImage();
		boundary = new EntityBoundary(this, image.getWidth(), image.getHeight(), x, y);
	}

}
