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
    private static final int LX = 24;
    private static final int LY = 42;
    private static final int ROW_OFFSET = 18;
    private static final byte TEXT_PADDING = 4;
    private static final Color BACKGROUND_COLOR = Color.black;
    private static final Font FONT = new TrueTypeFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 12), true);

    private static void drawLine(String s, Graphics graphics) {
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(LX - TEXT_PADDING, LY + (row * ROW_OFFSET) - FONT.getLineHeight(), graphics.getFont().getWidth(s) + (TEXT_PADDING * 2), FONT.getLineHeight() + TEXT_PADDING);
        graphics.setColor(Color.white);
        graphics.drawString(s, LX, LY + (row * ROW_OFFSET) - FONT.getLineHeight() + 4);
        row++;
    }

    /**
     * Prints the given object to the console, along with the current thread and time
     */
    public static void out(Object object) {
        System.out.println("[" + Thread.currentThread().getName() + " " + System.currentTimeMillis() + "] " + object);
    }

    /**
     * Prints the given object to the console with the tag "INFO"
     */
    public static void info(Object object) {
        out("INFO: " + object);
    }

    /**
     * Prints the given object to the console with the tag "ERROR"
     */
    public static void error(Object object) {
        out("ERROR: " + object);
    }

    /**
     * Handles the exception, printing out its message
     */
    public static void error(Exception e) {
        error(e.getMessage());
    }

    /**
     * Prints the given object to the console with the tag "WARNING"
     */
    public static void warning(Object object) {
        out("WARNING: " + object);
    }

    /**
     * Display debug information, toggled with F3
     */
    public static void render(Graphics graphics) {
        row = 0;
        graphics.setFont(FONT);
        drawLine("Location: " + InGameState.map.sector + " (" + Entities.PLAYER.x + ", " + Entities.PLAYER.y + ")", graphics);
        drawLine("Mouse: " + Mouse.x + ", " + Mouse.y, graphics);
        if (Entities.PLAYER.lastCollision != null) {
            drawLine("Last collision: " + Entities.PLAYER.lastCollision, graphics);
        }
        drawLine("Collisions: " + Collisions.getAmount(), graphics);
    }

}
