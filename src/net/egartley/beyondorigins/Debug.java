package net.egartley.beyondorigins;

import net.egartley.beyondorigins.data.EntityStore;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.maps.debug.DebugMap;
import net.egartley.gamelib.input.Mouse;

import java.awt.*;

public class Debug {

    /**
     * Font to use while rendering debug lines
     */
    private static Font font = new Font("Consolas", Font.PLAIN, 12);
    /**
     * Used for calculating width of strings so that each line's background will be sized correctly
     */
    private static FontMetrics fontMetrics;
    /**
     * Color for the background of debug lines
     */
    private static Color backgroundColor = Color.BLACK;
    /**
     * Whether or not font metrics have been set
     */
    private static boolean setFontMetrics;
    /**
     * Initial line x-coordinate
     */
    private static int lx = 24;
    /**
     * Initial line y-coordinate
     */
    private static int ly = 42;
    /**
     * Offset for each line
     */
    private static int rowOffset = 18;
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
     * @param object
     *         The object to print out
     */
    public static void out(Object object) {
        System.out.println(object);
    }

    /**
     * Prints the given object using {@link java.io.PrintStream#println(Object) System.out.println(object)} with the tag
     * "INFO"
     *
     * @param object
     *         The object to print out
     */
    public static void info(Object object) {
        out("INFO: " + object);
    }

    /**
     * Prints the given object using {@link java.io.PrintStream#println(Object) System.out.println(object)} with the tag
     * "WARNING"
     *
     * @param object
     *         The object to print out
     */
    public static void warning(Object object) {
        out("WARNING: " + object);
    }

    /**
     * Prints the given object using {@link java.io.PrintStream#println(Object) System.out.println(object)} with the tag
     * "ERROR"
     *
     * @param object
     *         The object to print out
     */
    public static void error(Object object) {
        out("ERROR: " + object);
    }

    private static void drawLine(String s, Graphics graphics) {
        if (!setFontMetrics) {
            fontMetrics = graphics.getFontMetrics();
            // don't do this every tick, only once
            setFontMetrics = true;
        }
        graphics.setColor(backgroundColor);
        // background
        graphics.fillRect(lx - TEXT_PADDING, ly + (row * rowOffset) - font.getSize(), fontMetrics.stringWidth(s) +
                (TEXT_PADDING * 2), font.getSize() + TEXT_PADDING);
        graphics.setColor(Color.WHITE);
        // draw line text
        graphics.drawString(s, lx, ly + (row * rowOffset));
        row++;
    }

    /**
     * Render debug information
     */
    public static void render(Graphics graphics) {
        if (Game.debug) {
            row = 0;
            graphics.setFont(font);
            drawLine("Position: " + Entities.PLAYER.x() + ", " + Entities.PLAYER.y(), graphics);
            // drawLine("Delta: " + Entities.PLAYER.deltaX + ", " + Entities.PLAYER.deltaY, graphics);
            drawLine("Location: " + DebugMap.currentSector, graphics);
            drawLine("Mouse: " + Mouse.x + ", " + Mouse.y, graphics);
            drawLine("EntityStore: " + EntityStore.amount, graphics);
            if (Entities.PLAYER.lastCollision != null) {
                drawLine("Last collision: " + Entities.PLAYER.lastCollision, graphics);
            }
            drawLine("isCollided = " + Entities.PLAYER.isCollided, graphics);
        }
    }

}
