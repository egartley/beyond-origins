package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Animation;
import net.egartley.beyondorigins.objects.Sprite;

public class Player extends AnimatedEntity {

	private final byte UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;
	private byte speed = 1, animationThreshold = 12;

	public Player(ArrayList<Sprite> sprites) {
		this.spriteCollection = sprites;
		currentSprite = sprites.get(0);
		setAnimationCollection();
	}

	private void move(byte direction) {
		if (currentAnimation.isStopped) {
			currentAnimation.restart();
		}
		switch (direction) {
		case UP:
			absoluteY -= speed;
			break;
		case DOWN:
			absoluteY += speed;
			break;
		case LEFT:
			absoluteX -= speed;
			currentAnimation = animationCollection.get(0);
			break;
		case RIGHT:
			absoluteX += speed;
			currentAnimation = animationCollection.get(1);
			break;
		default:
			break;
		}
	}

	@Override
	public void setAnimationCollection() {
		animationCollection.clear();
		for (Sprite s : spriteCollection) {
			Animation a = new Animation(s);
			a.setThreshold(animationThreshold);
			animationCollection.add(a);
		}
		currentAnimation = animationCollection.get(0);
	}

	@Override
	public void render(Graphics graphics) {
		currentAnimation.render(graphics, absoluteX, absoluteY);
	}

	@Override
	public void tick() {
		if (Keyboard.isPressed(KeyEvent.VK_W)) {
			move(UP);
		} else if (Keyboard.isPressed(KeyEvent.VK_A)) {
			move(LEFT);
		} else if (Keyboard.isPressed(KeyEvent.VK_S)) {
			move(DOWN);
		} else if (Keyboard.isPressed(KeyEvent.VK_D)) {
			move(RIGHT);
		} else {
			if (!currentAnimation.isStopped) {
				currentAnimation.stop();
			}
		}
		currentAnimation.tick();
	}

}
