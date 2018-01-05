package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Animation;
import net.egartley.beyondorigins.objects.Sprite;

public class Player extends AnimatedEntity {

	public final byte UP = 1;
	public final byte DOWN = 2;
	public final byte LEFT = 3;
	public final byte RIGHT = 4;

	private final byte LEFT_ANIMATION = 0;
	private final byte RIGHT_ANIMATION = 1;

	public double speed = 1;
	private byte animationThreshold = 11;
	private int maximumX;
	private int maximumY;

	public boolean isAllowedToMoveUpwards = true;
	public boolean isAllowedToMoveDownwards = true;
	public boolean isAllowedToMoveLeftwards = true;
	public boolean isAllowedToMoveRightwards = true;
	private boolean isMovingUpwards = false;
	private boolean isMovingDownwards = false;
	private boolean isMovingLeftwards = false;
	private boolean isMovingRightwards = false;

	public Player(ArrayList<Sprite> sprites) {
		super("Player");
		this.sprites = sprites;
		sprite = sprites.get(0);
		maximumX = Game.WINDOW_WIDTH;
		maximumY = Game.WINDOW_HEIGHT;
		setAnimationCollection();
		setBoundary();
		setCollisions();

		sectorSpecific = false;
		isDualRendered = false;
	}

	private void move(byte direction) {
		if (animation.isStopped == true) {
			// animation was stopped, so restart it because we're moving!
			animation.restart();
		}
		switch (direction) {
		case UP:
			if (boundary.top <= 0 || isAllowedToMoveUpwards == false) {
				break; // top of window or can't move upwards
			}
			y -= speed;
			break;
		case DOWN:
			if (boundary.bottom >= maximumY || isAllowedToMoveDownwards == false) {
				break; // bottom of window or can't move downwards
			}
			y += speed;
			break;
		case LEFT:
			if (boundary.left <= 0 || isAllowedToMoveLeftwards == false) {
				break; // left of window or can't move leftwards
			}
			x -= speed;
			switchAnimation(LEFT_ANIMATION);
			break;
		case RIGHT:
			if (boundary.right >= maximumX || isAllowedToMoveRightwards == false) {
				break; // right of window or can't move rightwards
			}
			x += speed;
			switchAnimation(RIGHT_ANIMATION);
			break;
		default:
			break;
		}
	}

	/**
	 * Changes the animation to another one in the collection
	 * 
	 * @param i
	 *            The index of the animation
	 */
	private void switchAnimation(byte i) {
		animation = animationCollection.get(i);
	}

	/**
	 * Allows the player to move in all directions
	 */
	public void allowAllMovement() {
		isAllowedToMoveUpwards = true;
		isAllowedToMoveDownwards = true;
		isAllowedToMoveLeftwards = true;
		isAllowedToMoveRightwards = true;
	}

	/**
	 * Returns whether or not the player is currently moving in a certain direction
	 * 
	 * @param direction
	 *            {@link #UP}, {@link #DOWN}, {@link #LEFT} or {@link #RIGHT}
	 * @return True if the player is moving in the given direction, false if not or
	 *         the given direction was unknown
	 */
	public boolean isMoving(byte direction) {
		switch (direction) {
		case UP:
			return isMovingUpwards;
		case DOWN:
			return isMovingDownwards;
		case LEFT:
			return isMovingLeftwards;
		case RIGHT:
			return isMovingRightwards;
		default:
			Debug.warning("Tried to get an unknown movement from the player (" + direction + "), expected " + UP + ", "
					+ DOWN + ", " + LEFT + " or " + RIGHT + "");
			return false;
		}
	}

	@Override
	public void setAnimationCollection() {
		animationCollection.clear();
		// this allows variations of the player sprite to be added in the future
		for (Sprite s : sprites) {
			Animation a = new Animation(s);
			a.setThreshold(animationThreshold);
			animationCollection.add(a);
		}
		animation = animationCollection.get(0);
	}

	@Override
	public void setBoundary() {
		boundary = new EntityBoundary(this, sprite.frameWidth, sprite.frameHeight, new BoundaryPadding(2, 4, 2, 4));
	}

	@Override
	public void render(Graphics graphics) {
		animation.render(graphics, (int) x, (int) y);
		drawDebug(graphics);
	}

	@Override
	public void tick() {
		// get keyboard input (typical W-A-S-D)
		boolean up = Keyboard.isPressed(KeyEvent.VK_W);
		boolean down = Keyboard.isPressed(KeyEvent.VK_S);
		boolean left = Keyboard.isPressed(KeyEvent.VK_A);
		boolean right = Keyboard.isPressed(KeyEvent.VK_D);
		// reset all booleans for player's current movement
		isMovingUpwards = false;
		isMovingDownwards = false;
		isMovingLeftwards = false;
		isMovingRightwards = false;
		// check if moving diagonal
		if ((up == true && left == true) || (up == true && right == true) || (down == true && left == true)
				|| (down == true && right == true)) {
			// slightly reduce speed to keep diagonal speed the same as when moving only one
			// direction
			speed = 0.95;
		}
		if (up) {
			isMovingUpwards = true;
			move(UP);
		} else if (down) {
			// cannot be moving up and down at the same time
			isMovingDownwards = true;
			move(DOWN);
		}
		if (left) {
			isMovingLeftwards = true;
			move(LEFT);
		} else if (right) {
			// cannot be moving left and right at the same time
			isMovingRightwards = true;
			move(RIGHT);
		}
		if (!left && !right && !down && !up) {
			// not moving at all, so stop the animation
			animation.stop();
		}
		// reset speed
		speed = 1.0;
		animation.tick();
		boundary.tick();
		effectiveX = boundary.left;
		effectiveY = boundary.top;
	}

	@Override
	protected void setCollisions() {

	}

	@Override
	public void drawSecondLayer(Graphics graphics) {

	}

	@Override
	public void drawFirstLayer(Graphics graphics) {

	}

}
