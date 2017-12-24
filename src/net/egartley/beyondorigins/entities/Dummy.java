package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

public class Dummy extends StaticEntity {

	public Dummy(Sprite sprite) {
		generateUUID();
		id = "Dummy";
		this.sprite = sprite;
		x = 470;
		y = 190;
		setBoundary();
	}

	@Override
	public void render(Graphics graphics)
	{
		graphics.drawImage(sprite.getCurrentFrameAsBufferedImage(), x, y, null);
		boundary.draw(graphics);
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void setBoundary()
	{
		BufferedImage image = sprite.getCurrentFrameAsBufferedImage();
		boundary = new EntityBoundary(this, image.getWidth(), image.getHeight(), new BoundaryPadding(12), x, y);
	}

	@Override
	protected void setCollisions()
	{
		
	}

}
