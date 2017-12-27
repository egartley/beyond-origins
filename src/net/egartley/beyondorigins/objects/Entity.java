package net.egartley.beyondorigins.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.logic.collision.Collision;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.logic.math.Calculate;

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

	public ArrayList<Collision>	collisions;
	/**
	 * The sprite to use while rendering
	 */
	public Sprite				sprite;
	/**
	 * This entity's boundary
	 */
	public EntityBoundary		boundary;
	/**
	 * The entity's x-axis coordinate (absolute)
	 */
	public int					x;
	/**
	 * The entity's y-axis coordinate (absolute)
	 */
	public int					y;
	/**
	 * The entity's "effective" x-axis coordinate (includes its boundary)
	 */
	public int					effectiveX;
	/**
	 * The entity's "effective" y-axis coordinate (includes its boundary)
	 */
	public int					effectiveY;
	/**
	 * The entity's unique ID number. Use {@link #id} for user-friendly
	 * identification
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
	 * @see {@link net.egartley.beyondorigins.logic.collision.Collision Collision}
	 * @see {@link net.egartley.beyondorigins.logic.collision.EntityEntityCollision
	 *      EntityEntityCollision}
	 */
	public boolean				isCollided;
	/**
	 * Human-readable identifier for this entity
	 */
	public String				id;
	/**
	 * This entity's most recent collision, or the current one if
	 * {@link #isCollided} is
	 * true
	 */
	public Collision			lastCollision;

	private String				name;
	private Font				nameTagFont		= new Font("Consalas", Font.PLAIN, 11);
	private boolean				setFontMetrics	= false;
	private int					nameTagWidth, entityWidth, nameX, nameY;

	/**
	 * Creates a new entity with a randomly generated UUID
	 */
	public Entity(String id) {
		generateUUID();
		this.id = id;
	}

	/**
	 * Returns this entity as a human-readable string
	 */
	public String toString()
	{
		return id + "#" + uuid;
	}

	/**
	 * <p>
	 * Method for actually rendering this entity
	 * </p>
	 * <p>
	 * {@link #sprite} should be used
	 * </p>
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object
	 */
	public abstract void render(Graphics graphics);

	/**
	 * Draws the entity's "name tag" which displays {@link #id} and {@link #uuid}
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object
	 */
	public void drawNameTag(Graphics graphics)
	{
		// init
		if (setFontMetrics == false) {
			name = this.toString();
			nameTagWidth = graphics.getFontMetrics(nameTagFont).stringWidth(name) + 8; // 4-pixel padding on both sides
			entityWidth = this.sprite.frameWidth;
			setFontMetrics = true;
		}
		nameX = Calculate.horizontalCenter(x, entityWidth) - Calculate.horizontalCenter(0, nameTagWidth);
		nameY = this.y - 18;

		graphics.setColor(new Color(0, 0, 0, 128));
		graphics.setFont(nameTagFont);

		graphics.fillRect(nameX, nameY, nameTagWidth, 16);
		graphics.setColor(Color.WHITE);
		graphics.drawString(name, nameX + 4, nameY + 11);
	}

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
	 * Sets this entity's collisions
	 */
	protected abstract void setCollisions();

	/**
	 * Method to generate {@link #uuid}
	 */
	private void generateUUID()
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
