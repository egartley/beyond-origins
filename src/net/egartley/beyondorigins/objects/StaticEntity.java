package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * An entity that cannot be animated
 * 
 * @author Evan Gartley
 * @see Entity
 */
public abstract class StaticEntity extends Entity {

	/**
	 * The entity's frame that is used while rendering
	 */
	public BufferedImage frame;

	/**
	 * Creates a new static entity, while setting {@link Entity#isAnimated} to false
	 * and {@link Entity#isStatic} to true
	 * 
	 * @param id
	 *            Human-readable ID for the entity
	 * @see Entity
	 */
	public StaticEntity(String id) {
		super(id);
		isAnimated = false;
		isStatic = true;
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(frame, (int) x, (int) y, null);
		drawDebug(graphics);
	}

}
