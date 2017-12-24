package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.egartley.beyondorigins.logic.collision.Collision;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

/**
 * A tree that can be displayed on a map. The player cannot walk over or under it
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
		generateUUID();
		id = "DefaultTree (" + uuid + ")";
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
	 * @param tree
	 *            {@link EntityBoundary} for the tree
	 * @see DefaultTree
	 * @see EntityBoundary
	 */
	public void onPlayerCollision(EntityEntityCollisionEvent event)
	{
		switch (event.collidedSide)
		{
			case EntityEntityCollisionEvent.RIGHT:
				Entities.PLAYER.canMoveLeft = false;
			break;
			case EntityEntityCollisionEvent.LEFT:
				Entities.PLAYER.canMoveRight = false;
			break;
			case EntityEntityCollisionEvent.TOP:
				Entities.PLAYER.canMoveDown = false;
			break;
			case EntityEntityCollisionEvent.BOTTOM:
				Entities.PLAYER.canMoveUp = false;
			break;
			default:
			break;
		}
	}

	@Override
	protected void setBoundary()
	{
		BufferedImage image = sprite.getCurrentFrameAsBufferedImage();
		boundary = new EntityBoundary(this, image.getWidth(), image.getHeight(), new BoundaryPadding(-24, -20, -6, -20), x, y);
	}

	@Override
	protected void setCollisions()
	{
		collisions = new ArrayList<Collision>();
		EntityEntityCollision withPlayer = new EntityEntityCollision(Entities.PLAYER.boundary, boundary)
			{
				public void onCollision(EntityEntityCollisionEvent event)
				{
					onPlayerCollision(event);
				};

				public void collisionEnd(EntityEntityCollisionEvent event)
				{
					Entities.PLAYER.enableAllMovement();
				};
			};
		collisions.add(withPlayer);
	}

	@Override
	public void render(Graphics graphics)
	{
		graphics.drawImage(sprite.getCurrentFrameAsBufferedImage(), x, y, null);
		boundary.draw(graphics);
	}

	@Override
	public void tick()
	{
		for (Collision collision : collisions) {
			collision.tick();
		}
	}

}
