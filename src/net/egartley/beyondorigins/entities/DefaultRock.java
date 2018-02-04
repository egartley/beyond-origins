package net.egartley.beyondorigins.entities;

import java.awt.Graphics;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

/**
 * Rock that the player cannot walk over
 * 
 * @author Evan Gartley
 */
public class DefaultRock extends StaticEntity {

	private EntityBoundary boundary;

	/**
	 * Creates a new rock, however this should only be used when first initializing
	 * 
	 * @param sprite
	 *            The rock's sprite
	 */
	public DefaultRock(Sprite sprite) {
		this(sprite, 0, 0);
	}

	/**
	 * Creates a new rock. This constructor should be used in map sector
	 * constructors or anywhere else that the rock has already been initialized
	 * 
	 * @param sprite
	 *            The rock's sprite
	 * @param x
	 *            The x-axis coordinate to set the rock at initailly
	 * @param y
	 *            The y-axis coordinate to set the rock at initailly
	 */
	public DefaultRock(Sprite sprite, double x, double y) {
		super("Rock");
		this.sprite = sprite;
		frame = this.sprite.getCurrentFrameAsBufferedImage();
		this.x = x;
		this.y = y;
		setBoundaries();
		setCollisions();

		// entity-specific stuff
		isSectorSpecific = true;
		isDualRendered = true;
		// set the first layer as the top half
		firstLayer = frame.getSubimage(0, frame.getHeight() / 2, frame.getWidth(), frame.getHeight() / 2);
		// set the second layer as the bottom half
		secondLayer = frame.getSubimage(0, 0, frame.getWidth(), frame.getHeight() / 2);
	}

	/**
	 * <p>
	 * Disables the direction of movement opposite of where the player collided with
	 * the rock
	 * </p>
	 * <p>
	 * <b>Example:</b> player collides with top of the rock, so downward movement is
	 * disabled
	 * </p>
	 * 
	 * @param event
	 *            The collision event between the player and rock
	 */
	private void onPlayerCollision(EntityEntityCollisionEvent event) {
		Entities.PLAYER.lastCollisionEvent = event;
		switch (event.collidedSide) {
		case EntityEntityCollisionEvent.RIGHT_SIDE:
			// collided on the right, so disable leftwards movement
			Entities.PLAYER.isAllowedToMoveLeftwards = false;
			break;
		case EntityEntityCollisionEvent.LEFT_SIDE:
			// collided on the left, so disable rightwards movement
			Entities.PLAYER.isAllowedToMoveRightwards = false;
			break;
		case EntityEntityCollisionEvent.TOP_SIDE:
			// collided at the top, so disable downwards movement
			Entities.PLAYER.isAllowedToMoveDownwards = false;
			break;
		case EntityEntityCollisionEvent.BOTTOM_SIDE:
			// collided at the bottom, so disable upwards movement
			Entities.PLAYER.isAllowedToMoveUpwards = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(frame, (int) x, (int) y, null);
		drawDebug(graphics);
	}

	@Override
	public void tick() {
		for (EntityEntityCollision collision : collisions) {
			collision.tick();
		}
	}

	@Override
	protected void setBoundaries() {
		boundary = new EntityBoundary(this, frame.getWidth(), frame.getHeight(), new BoundaryPadding(-4, -2, -8, -2));
		boundaries.add(boundary);
	}

	@Override
	protected void setCollisions() {
		EntityEntityCollision withPlayer = new EntityEntityCollision(Entities.PLAYER.headBoundary, boundary) {
			public void onCollide(EntityEntityCollisionEvent event) {
				onPlayerCollision(event);
			};

			public void onCollisionEnd(EntityEntityCollisionEvent event) {
				if (Entities.PLAYER.isCollided == false)
					Entities.PLAYER.allowAllMovement();
				else
					Entities.PLAYER.annulCollisionEvent(event);
			};
		};
		collisions.add(withPlayer);
		// add collisions with all of the player's boundaries, except the main one. The
		// events will be the same as the given "base" event
		for (EntityEntityCollision collision : Util.getAllBoundaryCollisions(withPlayer, Entities.PLAYER, boundary)) {
			if (collision.boundary1 != Entities.PLAYER.boundary)
				collisions.add(collision);
		}
	}

}
