package net.egartley.beyondorigins;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import net.egartley.beyondorigins.entities.Entities;

public class Debug {

	private static Font font = new Font("Consolas", Font.PLAIN, 12);
	private static FontMetrics fontMetrics;
	private static Color backgroundColor = new Color(0, 0, 0, 72);
	private static boolean setFontMetrics;
	private static int lx = 24, ly = 24, d = 18;

	/**
	 * Prints the given object using {@linkplain java.io.PrintStream#println(Object)
	 * System.out.println(object)}
	 * 
	 * @param object
	 *            {@link java.lang.Object Object}
	 */
	public static void out(Object object) {
		System.out.println(object);
	}

	/**
	 * Prints the given object using {@linkplain java.io.PrintStream#println(Object)
	 * System.out.println(object)} with the tag "INFO"
	 * 
	 * @param object
	 *            {@link java.lang.Object Object}
	 */
	public static void info(Object object) {
		out("INFO: " + object);
	}

	/**
	 * Prints the given object using {@linkplain java.io.PrintStream#println(Object)
	 * System.out.println(object)} with the tag "WARNING"
	 * 
	 * @param object
	 *            {@link java.lang.Object Object}
	 */
	public static void warning(Object object) {
		out("WARNING: " + object);
	}

	/**
	 * Prints the given object using {@linkplain java.io.PrintStream#println(Object)
	 * System.out.println(object)} with the tag "ERROR"
	 * 
	 * @param object
	 *            {@link java.lang.Object Object}
	 */
	public static void error(Object object) {
		out("ERROR: " + object);
	}

	private static void drawLine(String s, Graphics graphics, int r) {
		if (setFontMetrics == false) {
			fontMetrics = graphics.getFontMetrics();
			// don't do this every tick, only needs to be done once
			setFontMetrics = true;
		}
		int stringWidth = fontMetrics.stringWidth(s);
		graphics.setColor(backgroundColor);
		graphics.fillRect(lx - 4, ly + (r * d) - font.getSize(), stringWidth + 8, font.getSize() + 4);
		graphics.setColor(Color.WHITE);
		graphics.drawString(s, lx, ly + (r * d));
	}

	public static void render(Graphics graphics) {
		if (Game.debug) {
			graphics.setFont(font);
			drawLine("Player (" + (int) Entities.PLAYER.x + ", " + (int) Entities.PLAYER.y + "), (effective "
					+ Entities.PLAYER.effectiveX + ", " + Entities.PLAYER.effectiveY + ")", graphics, 0);
			drawLine("Collided: " + Entities.PLAYER.isCollided, graphics, 1);
			if (Entities.PLAYER.lastCollision != null)
				drawLine("Last collision: " + Entities.PLAYER.lastCollision.firstEntity + " and "
						+ Entities.PLAYER.lastCollision.secondEntity + ", collidedSide: "
						+ Entities.PLAYER.lastCollisionEvent.collidedSide, graphics, 2);
			// graphics.drawString("FPS: " + Game.getFPS(), lx, ly + (d * 2));
		}
	}

}
