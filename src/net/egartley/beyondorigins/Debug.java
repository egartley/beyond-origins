package net.egartley.beyondorigins;

import net.egartley.beyondorigins.core.input.Mouse;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class Debug {

    private static int row = 0;
    private static final int lx = 24;
    private static final int ly = 42;
    private static final int rowOffset = 18;
    private static final byte TEXT_PADDING = 4;
    private static final Color backgroundColor = Color.black;
    private static final Font font = new TrueTypeFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 12), true);

    /**
     * Prints the given object to the console, along with the current thread and time
     *
     * @param object The object to print out
     */
    public static void out(Object object) {
        System.out.println("[" + Thread.currentThread().getName() + " " + System.currentTimeMillis() + "] " + object);
    }

    /**
     * Prints the given object to the console with the tag "INFO"
     *
     * @param object The object to print out
     */
    public static void info(Object object) {
        out("INFO: " + object);
    }

    /**
     * Prints the given object to the console with the tag "WARNING"
     *
     * @param object The object to print out
     */
    public static void warning(Object object) {
        out("WARNING: " + object);
    }

    /**
     * Prints the given object to the console with the tag "ERROR"
     *
     * @param object The object to print out
     */
    public static void error(Object object) {
        out("ERROR: " + object);
    }

    /**
     * Handles the exception, printing out its message
     *
     * @param e The exception to handle
     * @see Exception#getMessage()
     */
    public static void error(Exception e) {
        error(e.getMessage());
    }

    private static void drawLine(String s, Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(lx - TEXT_PADDING, ly + (row * rowOffset) - font.getLineHeight(), graphics.getFont().getWidth(s) + (TEXT_PADDING * 2), font.getLineHeight() + TEXT_PADDING);
        graphics.setColor(Color.white);
        graphics.drawString(s, lx, ly + (row * rowOffset) - font.getLineHeight() + 4);
        row++;
    }

    /**
     * Display debug information, toggled with F3
     */
    public static void render(Graphics graphics) {
        row = 0;
        graphics.setFont(font);
        drawLine("Location: " + InGameState.map.sector + " (" + Entities.PLAYER.x() + ", " + Entities.PLAYER.y() + ")", graphics);
        drawLine("Mouse: " + Mouse.x + ", " + Mouse.y, graphics);
        if (Entities.PLAYER.lastCollision != null) {
            drawLine("Last collision: " + Entities.PLAYER.lastCollision, graphics);
        }
        drawLine("Collisions: " + Collisions.amount(), graphics);
    }

}
