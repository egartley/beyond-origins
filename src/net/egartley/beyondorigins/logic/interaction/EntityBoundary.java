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
	public Entity	entity;
	/**
	 * The current {@linkplain java.awt.Color Color} that is being used for
	 * {@link #draw(Graphics)}
	 */
	public Color	drawColor;

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
		entity = e;
		width = w + p.left + p.right;
		height = h + p.top + p.bottom;
		padding = p;
		x = entity.x - p.left;
		y = entity.y - p.top;
		top = y;
		bottom = top + height;
		left = x;
		right = left + width;
		setColor();
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
	 * @param x
	 *            An initial x-coordinate
	 * @param y
	 *            An initial y-coordinate
	 * @see Boundary
	 */
	public EntityBoundary(Entity e, int w, int h, BoundaryPadding p, int x, int y) {
		entity = e;
		width = w + p.left + p.right;
		height = h + p.top + p.bottom;
		padding = p;
		this.x = entity.x - p.left;
		this.y = entity.y - p.top;
		top = this.y;
		bottom = top + height;
		left = this.x;
		right = left + width;
		setColor();
	}

	private void setColor()
	{
		if (entity.isStatic) {
			drawColor = Color.BLACK;
		}
		else {
			drawColor = Color.YELLOW;
		}
	}

	@Override
	public void draw(Graphics graphics)
	{
		if (Game.drawBoundaries) {
			Color previous = graphics.getColor();
			graphics.setColor(drawColor);
			graphics.drawRect(x, y, width, height);
			graphics.setColor(previous);
		}
	}

	@Override
	public void tick()
	{
		x = entity.x - padding.left;
		y = entity.y - padding.top;
		top = y;
		bottom = top + height;
		left = x;
		right = left + width;
	}

}
