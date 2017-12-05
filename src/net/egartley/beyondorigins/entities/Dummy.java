package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

public class Dummy extends StaticEntity {

	public Dummy(Sprite sprite) {
		currentSprite = sprite;
		x = 470;
		y = 190;
		setBoundary();
	}

	@Override
	public void render(Graphics graphics)
	{
		graphics.drawImage(currentSprite.getCurrentFrameAsBufferedImage(), x, y, null);
		boundary.draw(graphics);
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void setBoundary()
	{
		BufferedImage image = currentSprite.getCurrentFrameAsBufferedImage();
		boundary = new EntityBoundary(this, image.getWidth(), image.getHeight(), 12, x, y);
	}

}
