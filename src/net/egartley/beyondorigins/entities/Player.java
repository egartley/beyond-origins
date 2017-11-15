package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Animation;
import net.egartley.beyondorigins.objects.Sprite;

public class Player extends AnimatedEntity {

	public Player(ArrayList<Sprite> sprites) {
		this.sprites = sprites;
		sprite = sprites.get(0);
		setOtherValues();
	}

	public Player(ArrayList<Sprite> sprites, Sprite current) {
		this.sprites = sprites;
		sprite = current;
		setOtherValues();
	}

	private void setOtherValues() {
		isAnimated = true;
		animation = new Animation(sprite);
	}

	@Override
	public void render(Graphics graphics) {
		// graphics.drawImage(sprite.getCurrentFrameAsBufferedImage(), absoluteX, absoluteY, null);
		animation.render(graphics, absoluteX, absoluteY);
	}

	@Override
	public void tick() {
		animation.tick();
	}

}
