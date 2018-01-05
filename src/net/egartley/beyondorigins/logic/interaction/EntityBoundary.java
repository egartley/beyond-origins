package net.egartley.beyondorigins.logic.interaction;

import java.awt.Color;
import java.awt.Graphics;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.objects.Entity;

/**
 * Represents a {@link Boundary} that is specifically tailored for use with an
 * {@link net.egartley.beyondorigins.objects.Entity Entity}
 * 
 * @author Evan Gartley
 * @see Boundary
 * @see {@link net.egartley.beyondorigins.objects.Entity Entity}
 * @see {@link net.egartley.beyondorigins.logic.interaction.BoundaryPadding
 *      BoundaryPadding}
 */
public class EntityBoundary extends Boundary {

	/**
	 * The entity in which to base this boundary around
	 * 
	 * @see {@link net.egartley.beyondorigins.objects.Entity Entity }
	 */
	public Entity parent;
	/**
	 * The current {@linkplain java.awt.Color Color} that is being used for
	 * {@link #draw(Graphics)}
	 */
	public Color drawColor;

	/**
	 * Creates a new boundary for the given entity (with no padding)
	 * 
	 * @param e
	 *            The entity to use
	 * @param w
	 *            Width of the boundary
	 * @param h
	 *            Height of the boundary
	 */
	public EntityBoundary(Entity e, int w, int h) {
		this(e, w, h, new BoundaryPadding(0));
	}

	/**
	 * Creates a new boundary for the given entity
	 * 
	 * @param e
	 *            The entity to use
	 * @param w
	 *            Width of the boundary (not counting the left or right padding)
	 * @param h
	 *            Height of the boundary (not counting the top or bottom padding)
	 * @param p
	 *            The padding to apply
	 * @see Boundary
	 */
	public EntityBoundary(Entity e, int w, int h, BoundaryPadding p) {
		parent = e;
		width = w + p.left + p.right;
		height = h + p.top + p.bottom;
		padding = p;
		x = (int) parent.x - p.left;
		y = (int) parent.y - p.top;
		top = y;
		bottom = top + height;
		left = x;
		right = left + width;
		setColor();
	}

	private void setColor() {
		if (parent.isStatic) {
			drawColor = Color.BLACK;
		} else {
			drawColor = Color.YELLOW;
		}
	}

	@Override
	public void draw(Graphics graphics) {
		if (Game.debug) {
			Color previous = graphics.getColor();
			graphics.setColor(drawColor);
			graphics.drawRect(x, y, width, height);
			graphics.setColor(previous);
		}
	}

	@Override
	public void tick() {
		x = (int) parent.x - padding.left;
		y = (int) parent.y - padding.top;
		top = y;
		bottom = top + height;
		left = x;
		right = left + width;
	}

}
