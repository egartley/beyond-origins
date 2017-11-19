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
	private final byte LEFT_ANIMATION = 0, RIGHT_ANIMATION = 1;
	private byte animationThreshold = 10;
	public double speed = 1.2;

	public Player(ArrayList<Sprite> sprites) {
		this.spriteCollection = sprites;
		currentSprite = sprites.get(0);
		setAnimationCollection();
	}

	private void move(byte direction) {
		if (animation.isStopped) {
			animation.restart();
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
			setAnimation(LEFT_ANIMATION);
			break;
		case RIGHT:
			absoluteX += speed;
			setAnimation(RIGHT_ANIMATION);
			break;
		default:
			break;
		}
	}
	
	private void setAnimation(byte i) {
		animation = animationCollection.get(i);
	}

	@Override
	public void setAnimationCollection() {
		animationCollection.clear();
		for (Sprite s : spriteCollection) {
			Animation a = new Animation(s);
			a.setThreshold(animationThreshold);
			animationCollection.add(a);
		}
		animation = animationCollection.get(0);
	}

	@Override
	public void render(Graphics graphics) {
		animation.render(graphics, (int) absoluteX, (int) absoluteY);
	}

	@Override
	public void tick() {
		boolean up = Keyboard.isPressed(KeyEvent.VK_W);
		boolean down = Keyboard.isPressed(KeyEvent.VK_S);
		boolean left = Keyboard.isPressed(KeyEvent.VK_A);
		boolean right = Keyboard.isPressed(KeyEvent.VK_D);
		if (up) {
			move(UP);
		} else if (down) {
			move(DOWN);
		}
		if (left) {
			move(LEFT);
		} else if (right) {
			move(RIGHT);
		} 
		if (!left && !right && !down && !up) {
			if (!animation.isStopped) {
				animation.stop();
			}
		}
		animation.tick();
	}

}
