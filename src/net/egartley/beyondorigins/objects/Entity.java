package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;

/**
 * A "thing" that can rendered with a sprite and change position
 * 
 * @author Evan Gartley
 * @see AnimatedEntity
 * @see StaticEntity
 */
public abstract class Entity {

	/**
	 * Collection of sprites that could be used while rendering
	 * 
	 * @see Sprite
	 */
	public ArrayList<Sprite>	spriteCollection;
	/**
	 * The sprite to use while rendering
	 */
	public Sprite				currentSprite;
	/**
	 * This entity's boundary
	 */
	public EntityBoundary		boundary;
	public int					x, y, uuid;
	/**
	 * Whether or not this entity is animated
	 * 
	 * @see AnimatedEntity
	 */
	public boolean				isAnimated;
	/**
	 * Whether or not this entity is static (no animation)
	 * 
	 * @see StaticEntity
	 */
	public boolean				isStatic;
	/**
	 * Whether ot not this entity is currently collided with another entity
	 * 
	 * @see {@link net.egartley.beyondorigins.logic.collision.EntityEntityCollision
	 *      EntityEntityCollision}
	 */
	public boolean				isCollided;
	/**
	 * Human-readable identifier for this entity
	 */
	public String				id;

	/**
	 * <p>
	 * Method for actually rendering this entity
	 * </p>
	 * <p>
	 * The current sprite ({@link #currentSprite}) should be used
	 * </p>
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object
	 */
	public abstract void render(Graphics graphics);

	/**
	 * Should be called 60 times per second in a tick thread
	 */
	public abstract void tick();

	/**
	 * Sets this entity's boundary
	 * 
	 * @see EntityBoundary
	 */
	protected abstract void setBoundary();

	/**
	 * Creates a new entity
	 */
	public Entity() {
		uuid = Util.randomInt(9999, 1000, true);
	}

	/**
	 * Sets {@link #currentSprite}
	 * 
	 * @param index
	 *            Index of a {@link Sprite} within {@link #spriteCollection} to set
	 *            as {@link #currentSprite}
	 */
	public void setCurrentSprite(int index)
	{
		currentSprite = spriteCollection.get(index);
	}

}
