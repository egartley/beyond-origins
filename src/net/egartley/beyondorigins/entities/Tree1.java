package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

public class Tree1 extends StaticEntity {
	
	public Tree1(Sprite s) {
		currentSprite = s;
		x = 200;
		y = 200;
		setBoundary();
	}

	@Override
	public void setBoundary() {
		BufferedImage image = currentSprite.getCurrentFrameAsBufferedImage();
		boundary = new EntityBoundary(this, image.getWidth(), image.getHeight(), 2, x, y);
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(currentSprite.getCurrentFrameAsBufferedImage(), x, y, null);
		boundary.draw(graphics);
	}

	@Override
	public void tick() {
		
	}

}
