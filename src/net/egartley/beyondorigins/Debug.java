package net.egartley.beyondorigins;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import net.egartley.beyondorigins.entities.Entities;

/**
 * Stuff for debugging
 * 
 * @author Evan Gartley
 */
public class Debug {

	private static Font font = new Font("Consolas", Font.PLAIN, 12);
	private static FontMetrics fontMetrics;
	private static Color backgroundColor = new Color(0, 0, 0, 72);
	private static boolean setFontMetrics;
	private static int lx = 24;
	private static int ly = 24;
	private static int d = 18;

	/**
	 * Prints the given object using {@link java.io.PrintStream#println(Object)
	 * System.out.println(object)}
	 * 
	 * @param object
	 *            The object to print out
	 */
	public static void out(Object object) {
		System.out.println(object);
	}

	/**
	 * Prints the given object using {@link java.io.PrintStream#println(Object)
	 * System.out.println(object)} with the tag "INFO"
	 * 
	 * @param object
	 *            The object to print out
	 */
	public static void info(Object object) {
		out("INFO: " + object);
	}

	/**
	 * Prints the given object using {@link java.io.PrintStream#println(Object)
	 * System.out.println(object)} with the tag "WARNING"
	 * 
	 * @param object
	 *            The object to print out
	 */
	public static void warning(Object object) {
		out("WARNING: " + object);
	}

	/**
	 * Prints the given object using {@link java.io.PrintStream#println(Object)
	 * System.out.println(object)} with the tag "ERROR"
	 * 
	 * @param object
	 *            The object to print out
	 */
	public static void error(Object object) {
		out("ERROR: " + object);
	}

	private static void drawLine(String s, Graphics graphics, int r) {
		if (setFontMetrics == false) {
			fontMetrics = graphics.getFontMetrics();
			// don't do this every tick, only once
			setFontMetrics = true;
		}
		graphics.setColor(backgroundColor);
		graphics.fillRect(lx - 4, ly + (r * d) - font.getSize(), fontMetrics.stringWidth(s) + 8, font.getSize() + 4);
		graphics.setColor(Color.WHITE);
		graphics.drawString(s, lx, ly + (r * d));
	}

	/**
	 * Render debug information
	 * 
	 * @param graphics
	 *            Graphics object to use
	 */
	public static void render(Graphics graphics) {
		if (Game.debug) {
			graphics.setFont(font);
			drawLine("Player (x = " + (int) Entities.PLAYER.x + ", y = " + (int) Entities.PLAYER.y + ")", graphics, 0);
			drawLine("Collided: " + Entities.PLAYER.isCollided, graphics, 1);
			if (Entities.PLAYER.lastCollision != null)
				drawLine("Last collision: " + Entities.PLAYER.lastCollision.boundary1 + " and "
						+ Entities.PLAYER.lastCollision.boundary2.parent + " (collidedSide = "
						+ Entities.PLAYER.lastCollisionEvent.collidedSide + ")", graphics, 2);
		}
	}

}
