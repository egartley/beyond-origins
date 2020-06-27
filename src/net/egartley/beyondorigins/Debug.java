package net.egartley.beyondorigins;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.input.Mouse;

import java.awt.*;

public class Debug {

    /**
     * Font to use while rendering debug lines
     */
    private static final Font font = new Font("Consolas", Font.PLAIN, 12);
    /**
     * Used for calculating width of strings so that each line's background will be sized correctly
     */
    private static FontMetrics fontMetrics;
    /**
     * Color for the background of debug lines
     */
    private static final Color backgroundColor = Color.BLACK;
    /**
     * Whether or not font metrics have been set
     */
    private static boolean setFontMetrics;
    /**
     * Initial line x-coordinate
     */
    private static final int lx = 24;
    /**
     * Initial line y-coordinate
     */
    private static final int ly = 42;
    /**
     * Offset for each line
     */
    private static final int rowOffset = 18;
    /**
     * What row to render the line on, for {@link #drawLine(String, Graphics)}
     */
    private static int row = 0;
    /**
     * Number of pixels to add as padding around each line of text
     */
    private static final byte TEXT_PADDING = 4;

    /**
     * Prints the given object using {@link java.io.PrintStream#println(Object) System.out.println(object)}
     *
     * @param object The object to print out
     */
    public static void out(Object object) {
        System.out.println("[" + Thread.currentThread().getName() + " " + System.currentTimeMillis() + "] " + object);
    }

    /**
     * Prints the given object using {@link java.io.PrintStream#println(Object) System.out.println(object)} with the tag
     * "INFO"
     *
     * @param object The object to print out
     */
    public static void info(Object object) {
        out("INFO: " + object);
    }

    /**
     * Prints the given object using {@link java.io.PrintStream#println(Object) System.out.println(object)} with the tag
     * "WARNING"
     *
     * @param object The object to print out
     */
    public static void warning(Object object) {
        out("WARNING: " + object);
    }

    /**
     * Prints the given object using {@link java.io.PrintStream#println(Object) System.out.println(object)} with the tag
     * "ERROR"
     *
     * @param object The object to print out
     */
    public static void error(Object object) {
        out("ERROR: " + object);
    }

    public static void error(Exception e) {
        error(e.getMessage());
    }

    private static void drawLine(String s, Graphics graphics) {
        if (!setFontMetrics) {
            fontMetrics = graphics.getFontMetrics();
            // don't do this every tick, only once
            setFontMetrics = true;
        }
        // background
        graphics.setColor(backgroundColor);
        graphics.fillRect(lx - TEXT_PADDING, ly + (row * rowOffset) - font.getSize(), fontMetrics.stringWidth(s) +
                (TEXT_PADDING * 2), font.getSize() + TEXT_PADDING);
        // draw line text
        graphics.setColor(Color.WHITE);
        graphics.drawString(s, lx, ly + (row * rowOffset));
        row++;
    }

    /**
     * Render debug information, toggled with F3
     */
    public static void render(Graphics graphics) {
        row = 0;
        graphics.setFont(font);
        drawLine("Location: " + Game.in().map.sector + " (" + Entities.PLAYER.x() + ", " + Entities.PLAYER.y() + ")", graphics);
        drawLine("Mouse: " + Mouse.x + ", " + Mouse.y, graphics);
        drawLine("Threads: " + Thread.activeCount(), graphics);
        if (Entities.PLAYER.lastCollision != null) {
            drawLine("Last collision: " + Entities.PLAYER.lastCollision, graphics);
        }
        drawLine("isCollided = " + Entities.PLAYER.isCollided, graphics);
    }

}
