package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryOffset;
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

	public final double SPEED = 1.6;

	private final byte LEFT_ANIMATION = 0;
	private final byte RIGHT_ANIMATION = 1;
	private final byte ANIMATION_THRESHOLD = 11;

	public EntityBoundary boundary;
	public EntityBoundary headBoundary;
	public EntityBoundary bodyBoundary;
	public EntityBoundary feetBoundary;

	public double speed;

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
		setBoundaries();
		setCollisions();

		isSectorSpecific = false;
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
	 * Disregards any movement restrictions imposed by the provided
	 * {@link net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent
	 * EntityEntityCollisionEvent}
	 * 
	 * @param event
	 *            The
	 *            {@link net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent
	 *            EntityEntityCollisionEvent} in which to annul
	 */
	public void annulCollisionEvent(EntityEntityCollisionEvent event) {
		switch (event.collidedSide) {
		case EntityEntityCollisionEvent.TOP_SIDE:
			isAllowedToMoveDownwards = true;
			break;
		case EntityEntityCollisionEvent.BOTTOM_SIDE:
			isAllowedToMoveUpwards = true;
			break;
		case EntityEntityCollisionEvent.LEFT_SIDE:
			isAllowedToMoveRightwards = true;
			break;
		case EntityEntityCollisionEvent.RIGHT_SIDE:
			isAllowedToMoveLeftwards = true;
			break;
		default:
			break;
		}
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
			a.setThreshold(ANIMATION_THRESHOLD);
			animationCollection.add(a);
		}
		animation = animationCollection.get(0);
	}

	@Override
	public void setBoundaries() {
		boundary = new EntityBoundary(this, sprite.frameWidth, sprite.frameHeight, new BoundaryPadding(4, 3, 2, 3));
		boundary.name = "Base";
		headBoundary = new EntityBoundary(this, 19, 18, new BoundaryPadding(0, 0, 0, 0),
				new BoundaryOffset(0, 0, 0, 5));
		headBoundary.name = "Head";
		bodyBoundary = new EntityBoundary(this, 30, 22, new BoundaryPadding(0, 0, 0, 0),
				new BoundaryOffset(0, 13, 0, 0));
		bodyBoundary.name = "Body";
		feetBoundary = new EntityBoundary(this, 17, 16, new BoundaryPadding(0, 0, 0, 0),
				new BoundaryOffset(0, 29, 0, 6));
		feetBoundary.name = "Feet";
		boundaries.add(boundary);
		boundaries.add(headBoundary);
		boundaries.add(bodyBoundary);
		boundaries.add(feetBoundary);
	}

	@Override
	public void render(Graphics graphics) {
		animation.render(graphics, (int) x, (int) y);
		drawDebug(graphics);
	}

	@Override
	public void tick() {
		// get keyboard input (typical W-A-S-D)
		boolean up = Keyboard.isKeyPressed(KeyEvent.VK_W);
		boolean down = Keyboard.isKeyPressed(KeyEvent.VK_S);
		boolean left = Keyboard.isKeyPressed(KeyEvent.VK_A);
		boolean right = Keyboard.isKeyPressed(KeyEvent.VK_D);
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
			speed = SPEED - 0.05;
		} else {
			speed = SPEED;
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

		animation.tick();
		for (EntityBoundary boundary : boundaries) {
			boundary.tick();
		}

		effectiveX = boundary.left;
		effectiveY = boundary.top;
	}

	@Override
	protected void setCollisions() {
		// nothing here right now because the player is not sector-specific, therefore
		// sector-specific entities are to define collisions with the player

		// this could change in the future, though
	}

	@Override
	public void drawFirstLayer(Graphics graphics) {

	}

	@Override
	public void drawSecondLayer(Graphics graphics) {
		// armor? different clothes? accessories?
	}

}
