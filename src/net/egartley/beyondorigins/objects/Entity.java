package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.logic.collision.Collision;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;

/**
 * An entity that can rendered with a sprite and have a specific position
 * 
 * @author Evan Gartley
 * @see Sprite
 * @see AnimatedEntity
 * @see StaticEntity
 */
public abstract class Entity {

	/**
	 * Collection of sprites that could be used while rendering
	 * 
	 * @see Sprite
	 */
	public ArrayList<Sprite>	sprites;
	
	public ArrayList<Collision> collisions;
	/**
	 * The sprite to use while rendering
	 */
	public Sprite				sprite;
	/**
	 * This entity's boundary
	 */
	public EntityBoundary		boundary;
	/**
	 * The entity's x-axis coordinate
	 */
	public int					x;
	/**
	 * The entity's y-axis coordinate
	 */
	public int					y;
	/**
	 * The entity's unique ID number. Use {@link #id} for user-friendly identification
	 */
	public int					uuid;
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
	 * Returns this entity as a human-readable string
	 */
	public String toString() {
		return id + " (" + uuid + ")";
	}

	/**
	 * <p>
	 * Method for actually rendering this entity
	 * </p>
	 * <p>
	 * The current sprite ({@link #sprite}) should be used
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
	
	protected abstract void setCollisions();

	/**
	 * Method to generate {@link #uuid}
	 */
	protected void generateUUID()
	{
		uuid = Util.randomInt(9999, 1000, true);
	}

	/**
	 * Sets {@link #sprite}
	 * 
	 * @param index
	 *            Index of a {@link Sprite} within {@link #sprites} to set
	 *            as {@link #sprite}
	 */
	public void setCurrentSprite(int index)
	{
		sprite = sprites.get(index);
	}

}
