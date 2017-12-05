package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

/**
 * A tree that can be displayed on a map. It also serves as a barrier to the
 * player
 * 
 * @author Evan Gartley
 * @see StaticEntity
 */
public class Tree1 extends StaticEntity {

	/**
	 * Creates a new instance of {@link Tree1} with the provided {@link Sprite}
	 * 
	 * @param sprite
	 *            {@link Sprite} object to use while rendering
	 */
	public Tree1(Sprite sprite) {
		currentSprite = sprite;
		setBoundary();
	}

	/**
	 * Creates a new instance of {@link Tree1} with the provided {@link Sprite} at
	 * the supplied coordinates
	 * 
	 * @param sprite
	 *            {@link Sprite} object to use while rendering
	 */
	public Tree1(Sprite sprite, int x, int y) {
		currentSprite = sprite;
		this.x = x;
		this.y = y;
		setBoundary();
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
	 * @see Tree1
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
		BufferedImage image = currentSprite.getCurrentFrameAsBufferedImage();
		// negative padding works!
		boundary = new EntityBoundary(this, image.getWidth(), image.getHeight(), -16, x, y);
	}

	@Override
	public void render(Graphics graphics)
	{
		graphics.drawImage(currentSprite.getCurrentFrameAsBufferedImage(), x, y, null);
		boundary.draw(graphics);
	}

	@Override
	public void tick()
	{

	}

}
