package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Animation;
import net.egartley.beyondorigins.objects.Sprite;

public class Player extends AnimatedEntity {

	public final byte	UP					= 1, DOWN = 2, LEFT = 3, RIGHT = 4;
	private final byte	LEFT_ANIMATION		= 0, RIGHT_ANIMATION = 1;
	private byte		animationThreshold	= 10;
	public byte			speed				= 1;
	private byte		boundaryPadding		= 5;
	private int			maxX, maxY;
	public boolean		canMoveUp			= true, canMoveDown = true, canMoveLeft = true, canMoveRight = true;
	public boolean		movingUp			= false, movingDown = false, movingLeft = false, movingRight = false;

	public Player(ArrayList<Sprite> sprites) {
		spriteCollection = sprites;
		currentSprite = sprites.get(0);
		maxX = Game.WINDOW_WIDTH;
		maxY = Game.WINDOW_HEIGHT;
		setAnimationCollection();
		setBoundary();
	}

	private void move(byte direction)
	{
		if (animation.isStopped) {
			animation.restart();
		}
		switch (direction)
		{
			case UP:
				if (boundary.top <= 0 || !canMoveUp) {
					break; // top of window
				}
				y -= speed;
			break;
			case DOWN:
				if (boundary.bottom >= maxY || !canMoveDown) {
					break; // bottom of window
				}
				y += speed;
			break;
			case LEFT:
				if (boundary.left <= 0 || !canMoveLeft) {
					break; // left of window
				}
				x -= speed;
				setAnimation(LEFT_ANIMATION);
			break;
			case RIGHT:
				if (boundary.right >= maxX || !canMoveRight) {
					break; // right of window
				}
				x += speed;
				setAnimation(RIGHT_ANIMATION);
			break;
			default:
			break;
		}
	}

	private void setAnimation(byte i)
	{
		animation = animationCollection.get(i);
	}

	public void enableAllMovement()
	{
		canMoveUp = true;
		canMoveDown = true;
		canMoveLeft = true;
		canMoveRight = true;
	}

	@Override
	public void setAnimationCollection()
	{
		animationCollection.clear();
		for (Sprite s : spriteCollection) {
			Animation a = new Animation(s);
			a.setThreshold(animationThreshold);
			animationCollection.add(a);
		}
		animation = animationCollection.get(0);
	}

	@Override
	public void setBoundary()
	{
		boundary = new EntityBoundary(this, currentSprite.frameWidth, currentSprite.frameHeight, boundaryPadding);
	}

	@Override
	public void render(Graphics graphics)
	{
		animation.render(graphics, x, y);
		boundary.draw(graphics);
	}

	@Override
	public void tick()
	{
		boolean up = Keyboard.isPressed(KeyEvent.VK_W);
		boolean down = Keyboard.isPressed(KeyEvent.VK_S);
		boolean left = Keyboard.isPressed(KeyEvent.VK_A);
		boolean right = Keyboard.isPressed(KeyEvent.VK_D);
		movingUp = false;
		movingDown = false;
		movingLeft = false;
		movingRight = false;
		if (up) {
			movingUp = true;
			move(UP);
		}
		else if (down) {
			movingDown = true;
			move(DOWN);
		}
		if (left) {
			movingLeft = true;
			move(LEFT);
		}
		else if (right) {
			movingRight = true;
			move(RIGHT);
		}
		if (!left && !right && !down && !up) {
			if (!animation.isStopped) {
				animation.stop();
			}
		}
		animation.tick();
		boundary.tick();
	}

}
