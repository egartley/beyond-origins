package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.logic.collision.Collision;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.CollisionEvent;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

/**
 * A rock that the player cannot walk over
 * 
 * @author Evan Gartley
 * @see StaticEntity
 */
public class DefaultRock extends StaticEntity {

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
	 *            The rock's sprite (use Entities.ROCK.sprite)
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
		setBoundary();
		setCollisions();

		// entity-specific stuff
		sectorSpecific = true;
		isDualRendered = true;
		// set the first layer as the top half
		firstLayer = frame.getSubimage(0, 0, frame.getWidth(), frame.getHeight() / 2);
		// set the second layer as the bottom half
		secondLayer = frame.getSubimage(0, frame.getHeight() / 2, frame.getWidth(), frame.getHeight() / 2);
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
		case EntityEntityCollisionEvent.RIGHT:
			// collided on the right, so disable leftwards movement
			Entities.PLAYER.isAllowedToMoveLeftwards = false;
			break;
		case EntityEntityCollisionEvent.LEFT:
			// collided on the left, so disable rightwards movement
			Entities.PLAYER.isAllowedToMoveRightwards = false;
			break;
		case EntityEntityCollisionEvent.TOP:
			// collided at the top, so disable downwards movement
			Entities.PLAYER.isAllowedToMoveDownwards = false;
			break;
		case EntityEntityCollisionEvent.BOTTOM:
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
		for (Collision collision : collisions) {
			collision.tick();
		}
	}

	@Override
	protected void setBoundary() {
		boundary = new EntityBoundary(this, frame.getWidth(), frame.getHeight(), new BoundaryPadding(-4, -2, -14, -2));
	}

	@Override
	protected void setCollisions() {
		collisions = new ArrayList<Collision>();
		// this is independent of whatever map/sector the player is in
		EntityEntityCollision withPlayer = new EntityEntityCollision(Entities.PLAYER, this) {
			public void onCollide(CollisionEvent event) {
				EntityEntityCollisionEvent e = (EntityEntityCollisionEvent) event;
				Entities.PLAYER.lastCollision = (EntityEntityCollision) e.invoker;
				firstEntity.isCollided = true;
				secondEntity.isCollided = true;
				onPlayerCollision(e);
			};

			public void onCollisionEnd(CollisionEvent event) {
				firstEntity.isCollided = false;
				secondEntity.isCollided = false;
				Entities.PLAYER.allowAllMovement();
			};
		};
		collisions.add(withPlayer);
	}

	@Override
	public void drawSecondLayer(Graphics graphics) {
		graphics.drawImage(firstLayer, (int) x, (int) y, null);
		drawDebug(graphics);
	}

	@Override
	public void drawFirstLayer(Graphics graphics) {
		graphics.drawImage(secondLayer, (int) x, (int) y + firstLayer.getHeight(), null);
	}

}
