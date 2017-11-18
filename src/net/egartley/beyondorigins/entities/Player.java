package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Animation;
import net.egartley.beyondorigins.objects.Sprite;

public class Player extends AnimatedEntity {

	public Player(ArrayList<Sprite> sprites) {
		this.spriteCollection = sprites;
		currentSprite = sprites.get(0);
		setAnimationCollection();
	}

	public Player(ArrayList<Sprite> sprites, Sprite current) {
		this.spriteCollection = sprites;
		currentSprite = current;
		setAnimationCollection();
	}

	@Override
	public void setAnimationCollection() {
		animationCollection.clear();
		for (Sprite s : spriteCollection) {
			animationCollection.add(new Animation(s));
		}
		currentAnimation = animationCollection.get(0);
	}

	@Override
	public void render(Graphics graphics) {
		currentAnimation.render(graphics, absoluteX, absoluteY);
	}

	@Override
	public void tick() {
		currentAnimation.tick();
	}

}
