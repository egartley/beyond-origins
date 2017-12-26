package net.egartley.beyondorigins;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import net.egartley.beyondorigins.entities.Entities;

public class Debug {

	private static Font	font	= new Font("Consalas", Font.BOLD, 12);
	private static int	lx		= 24, ly = 24, d = 18;

	/**
	 * Prints the given object using
	 * {@linkplain java.io.PrintStream#println(Object) System.out.println(object)}
	 * 
	 * @param object
	 *            {@link java.lang.Object Object}
	 */
	public static void out(Object object)
	{
		System.out.println(object);
	}

	/**
	 * Prints the given object using
	 * {@linkplain java.io.PrintStream#println(Object) System.out.println(object)}
	 * with the tag "INFO"
	 * 
	 * @param object
	 *            {@link java.lang.Object Object}
	 */
	public static void info(Object object)
	{
		out("INFO: " + object);
	}

	/**
	 * Prints the given object using
	 * {@linkplain java.io.PrintStream#println(Object) System.out.println(object)}
	 * with the tag "WARNING"
	 * 
	 * @param object
	 *            {@link java.lang.Object Object}
	 */
	public static void warning(Object object)
	{
		out("WARNING: " + object);
	}

	/**
	 * Prints the given object using
	 * {@linkplain java.io.PrintStream#println(Object) System.out.println(object)}
	 * with the tag "ERROR"
	 * 
	 * @param object
	 *            {@link java.lang.Object Object}
	 */
	public static void error(Object object)
	{
		out("ERROR: " + object);
	}

	public static void render(Graphics graphics)
	{
		if (Game.debug) {
			graphics.setColor(Color.WHITE);
			graphics.setFont(font);
			graphics.drawString("Player X: " + Entities.PLAYER.x + ", Y: " + Entities.PLAYER.y + " (eX: " + Entities.PLAYER.effectiveX + ", eY: "
					+ Entities.PLAYER.effectiveY + ")", lx, ly);
			String s = "isCollided: " + Entities.PLAYER.isCollided;
			if (Entities.PLAYER.isCollided && Entities.PLAYER.lastCollision != null)
				s += " (" + Entities.PLAYER.lastCollision.firstEntity + " and " + Entities.PLAYER.lastCollision.secondEntity + ", collidedSide: "
						+ Entities.PLAYER.lastCollisionEvent.collidedSide + ")";
			graphics.drawString(s, lx, ly + d);
		}
	}

}
