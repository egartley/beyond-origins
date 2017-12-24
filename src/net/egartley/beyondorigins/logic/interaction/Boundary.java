package net.egartley.beyondorigins.logic.interaction;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * An imaginary boundary, or border, with a width, height and coordinates
 * 
 * @author Evan Gartley
 * @see EntityBoundary
 * @see {@link net.egartley.beyondorigins.logic.interaction.BoundaryPadding
 *      BoundaryPadding}
 */
public abstract class Boundary {

	/**
	 * The absolute x-coordinate
	 */
	public int				x;
	/**
	 * The absolute y-coordinate
	 */
	public int				y;
	/**
	 * This boundary's width (calcuated with padding)
	 */
	public int				width;
	/**
	 * This boundary's height (calcuated with padding)
	 */
	public int				height;
	/**
	 * The "top" side of this boundary, which is its y-coordinate
	 */
	public int				top;
	/**
	 * The "right" side of this boundary, which is its x-coordinate
	 */
	public int				right;
	/**
	 * The "bottom" side of this boundary, which is its y-coordinate
	 */
	public int				bottom;
	/**
	 * The "left" side of this boundary, which is its x-coordinate
	 */
	public int				left;

	/**
	 * This boundary's padding (extra space added/subtracted from any or all of the
	 * four sides)
	 * 
	 * @see {@link net.egartley.beyondorigins.logic.interaction.BoundaryPadding
	 *      BoundaryPadding}
	 */
	public BoundaryPadding	padding;

	/**
	 * Use {@link java.awt.Graphics#drawRect(int, int, int, int) drawRect}
	 * method to render this boundary (only if
	 * {@link net.egartley.beyondorigins.Game#drawBoundaries Game.drawBoundaries} is
	 * enabled)
	 * 
	 * @param graphics
	 *            {@link java.awt.Graphics Graphics}
	 */
	public abstract void draw(Graphics graphics);

	/**
	 * Update {@linkplain #x}, {@linkplain #y}, {@linkplain #top},
	 * {@linkplain #left}, {@linkplain #bottom}
	 * and {@link #right}
	 */
	public abstract void tick();

	/**
	 * Returns the boundary as a {@link java.awt.Rectangle Rectangle} object
	 * 
	 * @return {@link java.awt.Rectangle Rectangle}
	 */
	public Rectangle asRectangle()
	{
		return new Rectangle(x, y, width, height);
	}

}
