package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
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
 * A tree that can be displayed on a map. The player cannot walk over or under
 * it
 * it
 * 
 * @author Evan Gartley
 * @see StaticEntity
 */
public class DefaultTree extends StaticEntity {

	/**
	 * Creates a new instance of {@link DefaultTree} with the provided
	 * {@link Sprite}
	 * 
	 * @param sprite
	 *            {@link Sprite} object to use while rendering
	 */
	public DefaultTree(Sprite sprite) {
		this(sprite, 0, 0);
	}

	/**
	 * Creates a new instance of {@link DefaultTree} with the provided
	 * {@link Sprite} at the supplied coordinates
	 * 
	 * @param sprite
	 *            {@link Sprite} object to use while rendering
	 */
	public DefaultTree(Sprite sprite, int x, int y) {
		super("DT");
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		setBoundary();
		setCollisions();
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
	 *            The collision event between the player and tree
	 */
	public void onPlayerCollision(EntityEntityCollisionEvent event)
	{
		Entities.PLAYER.lastCollisionEvent = event;
		switch (event.collidedSide)
		{
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
	protected void setBoundary()
	{
		// the image is set to a variable because it is used twice in the constructor
		// for the entity boundary, makes it more resource efficient (in theory)
		BufferedImage image = sprite.getCurrentFrameAsBufferedImage();
		boundary = new EntityBoundary(this, image.getWidth(), image.getHeight(), new BoundaryPadding(-24, -20, -6, -20), x, y);
	}

	@Override
	protected void setCollisions()
	{
		collisions = new ArrayList<Collision>();
		// this is independent of whatever map/sector the player is in
		EntityEntityCollision withPlayer = new EntityEntityCollision(Entities.PLAYER.boundary, boundary)
			{
				public void onCollide(CollisionEvent event)
				{
					Entities.PLAYER.lastCollision = (EntityEntityCollision) event.invoker;
					firstEntity.isCollided = true;
					secondEntity.isCollided = true;
					onPlayerCollision((EntityEntityCollisionEvent) event);
				};

				public void onCollisionEnd(CollisionEvent event)
				{
					firstEntity.isCollided = false;
					secondEntity.isCollided = false;
					Entities.PLAYER.allowAllMovement();
				};
			};
		collisions.add(withPlayer);
	}

	@Override
	public void render(Graphics graphics)
	{
		graphics.drawImage(sprite.getCurrentFrameAsBufferedImage(), x, y, null);
		drawDebug(graphics);
	}

	@Override
	public void tick()
	{
		for (Collision collision : collisions) {
			collision.tick();
		}
	}

}
