package net.egartley.beyondorigins.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.entities.EntityStore;
import net.egartley.beyondorigins.logic.collision.Collision;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.logic.math.Calculate;

/**
 * An object or character that can rendered with a sprite and have a specific
 * position
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
	public ArrayList<Sprite> sprites;

	public ArrayList<Collision> collisions;
	/**
	 * The sprite to use while rendering
	 */
	public Sprite sprite;
	/**
	 * If this entity is dual rendered, render this before the player
	 */
	public BufferedImage firstLayer;
	/**
	 * If this entity is dual rendered, render this after the player
	 */
	public BufferedImage secondLayer;
	/**
	 * This entity's boundary
	 */
	public EntityBoundary boundary;
	/**
	 * The most recent collision that has occured for this entity. If no collisions
	 * have occured within this entity's lifetime, this will be null
	 */
	public EntityEntityCollision lastCollision = null;
	/**
	 * The most recent collision event to have occured. This will be null if no
	 * collision event has yet to take place
	 */
	public EntityEntityCollisionEvent lastCollisionEvent = null;
	/**
	 * The entity's x-axis coordinate (absolute)
	 */
	public double x;
	/**
	 * The entity's y-axis coordinate (absolute)
	 */
	public double y;
	/**
	 * The entity's "effective" x-axis coordinate (includes its boundary)
	 */
	public int effectiveX;
	/**
	 * The entity's "effective" y-axis coordinate (includes its boundary)
	 */
	public int effectiveY;
	/**
	 * The entity's unique identifacation number. Use {@link #id} for user-friendly
	 * identification
	 */
	public int uuid;
	/**
	 * Whether or not this entity is animated
	 */
	public boolean isAnimated;
	/**
	 * Whether or not this entity is static (no animation)
	 */
	public boolean isStatic;
	/**
	 * Whether ot not this entity is currently collided with another entity
	 */
	public boolean isCollided;
	/**
	 * Whether or not this entity has two different "layers" that are rendered
	 * before and after the player
	 */
	public boolean isDualRendered;
	/**
	 * Whether or not this entity is currently registered in the entity store
	 */
	public boolean isRegistered;
	/**
	 * Whether or not this entity is "bound" to, or only exists in, a specific map
	 * sector
	 */
	public boolean sectorSpecific;
	/**
	 * Human-readable identifier for this entity
	 */
	public String id;

	private String name;
	private Font nameTagFont = new Font("Arial", Font.PLAIN, 11);
	private Color nameTagBackgroundColor = new Color(0, 0, 0, 128);
	private boolean setFontMetrics = false;
	private int nameTagWidth, entityWidth, nameX, nameY;

	/**
	 * Creates a new entity with a randomly generated UUID
	 */
	public Entity(String id) {
		generateUUID();
		this.id = id;
		EntityStore.register(this);
	}

	/**
	 * Returns this entity as a human-readable string (useful while debugging)
	 */
	public String toString() {
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
	 * Draws the second "layer" if dual rendered is set to true
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object
	 */
	public abstract void drawFirstLayer(Graphics graphics);

	/**
	 * Draws the first "layer" if dual rendered is set to true (with offset y
	 * coordinate)
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object
	 */
	public abstract void drawSecondLayer(Graphics graphics);

	/**
	 * Draws debug information, such as the entity's boundary and "name tag"
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object
	 */
	public void drawDebug(Graphics graphics) {
		if (Game.debug) {
			boundary.draw(graphics);
			drawNameTag(graphics);
		}
	}

	/**
	 * "Kills" this entity by removing it from the entity store. This should only be
	 * used for sector-specific entities
	 */
	public void kill() {
		EntityStore.remove(this);
	}

	/**
	 * Draws the entity's "name tag", which displays {@link #id} and {@link #uuid}
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object
	 */
	private void drawNameTag(Graphics graphics) {
		// init
		if (setFontMetrics == false) {
			name = toString();
			nameTagWidth = graphics.getFontMetrics(nameTagFont).stringWidth(name) + 8; // 4 pixel padding on both sides
			entityWidth = sprite.frameWidth;
			// don't initialize again, don't need to
			setFontMetrics = true;
		}
		nameX = Calculate.horizontalCenter((int) x, entityWidth) - Calculate.horizontalCenter(0, nameTagWidth);
		nameY = (int) y - 18;

		graphics.setColor(nameTagBackgroundColor);
		graphics.setFont(nameTagFont);

		graphics.fillRect(nameX, nameY, nameTagWidth, 18);
		graphics.setColor(Color.WHITE);
		graphics.drawString(name, nameX + 5, nameY + 13);
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
	 * Generates, and then sets, a new UUID
	 * 
	 * @see #uuid
	 */
	public void generateUUID() {
		uuid = Util.randomInt(9999, 1000, true);
	}

	/**
	 * Sets the current sprite
	 * 
	 * @param index
	 *            Position of a sprite within the sprite collection to set as the
	 *            current one
	 */
	public void setCurrentSprite(int index) {
		sprite = sprites.get(index);
	}

}
