package net.egartley.beyondorigins.entities;

import java.util.ArrayList;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

/**
 * A tree that the player cannot walk over
 * 
 * @author Evan Gartley
 * @see StaticEntity
 */
public class DefaultTree extends StaticEntity {

	private EntityBoundary boundary;

	/**
	 * Creates a new default tree
	 * 
	 * @param sprite
	 *            {@link Sprite} to use while rendering
	 */
	public DefaultTree(Sprite sprite) {
		this(sprite, 0.0, 0.0);
	}

	/**
	 * Creates a new instance of {@link DefaultTree} with the provided
	 * {@link Sprite} at the supplied coordinates
	 * 
	 * @param sprite
	 *            {@link net.egartley.beyondorigins.objects.Sprite Sprite} to use
	 *            while rendering
	 * @param x
	 *            The x-coordinate to render at
	 * @param y
	 *            The y-coordinate to render at
	 */
	public DefaultTree(Sprite sprite, double x, double y) {
		super("Tree");
		this.sprite = sprite;
		frame = this.sprite.getCurrentFrameAsBufferedImage();
		this.x = x;
		this.y = y;
		setBoundaries();
		setCollisions();

		isSectorSpecific = true;
		// set the first layer as the leaves
		firstLayer = frame.getSubimage(0, 44, frame.getWidth(), 20);
		// set the second layer as the trunk
		secondLayer = frame.getSubimage(0, 0, frame.getWidth(), 44);
	}

	/**
	 * <p>
	 * Disables the direction of movement opposite of where the player collided with
	 * the tree
	 * </p>
	 * <p>
	 * <b>Example:</b> player collides with top of the tree, so downward movement is
	 * disabled
	 * </p>
	 * 
	 * @param event
	 *            The
	 *            {@link net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent
	 *            EntityEntityCollisionEvent} between the player and tree
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
	protected void setBoundaries() {
		boundary = new EntityBoundary(this, frame.getWidth(), frame.getHeight(),
				new BoundaryPadding(-24, -24, -36, -24));
		boundaries.add(boundary);
	}

	@Override
	protected void setCollisions() {
		collisions = new ArrayList<EntityEntityCollision>();
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
		for (EntityEntityCollision collision : Util.getAllBoundaryCollisions(withPlayer, Entities.PLAYER, boundary)) {
			if (collision.boundary1 != Entities.PLAYER.boundary)
				collisions.add(collision);
		}
	}

	@Override
	public void tick() {
		for (EntityEntityCollision collision : collisions) {
			collision.tick();
		}
	}

}
