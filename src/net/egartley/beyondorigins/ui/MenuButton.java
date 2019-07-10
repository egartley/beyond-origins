package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.gamelib.input.Mouse;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuButton {

    /**
     * Whether or not this button is able to be clicked
     */
    public boolean isEnabled;
    /**
     * Whether or not font metrics (string width and location) have been calculated. This is meant to avoid calculating the same thing every tick
     */
    public boolean setFontMetrics;
    private int x, y, width, height, stringX, stringY, stringWidth;

    public static Font font = new Font("Georgia", Font.BOLD, 24);

    /**
     * String that is displayed on the button
     */
    private String text;
    private Color disabledColor = Color.DARK_GRAY;
    private Color enabledColor = Color.RED.darker();
    private Color hoverColor = enabledColor.brighter();
    private Color textColor = new Color(0x0092ff);
    /**
     * The color that is currently being used
     */
    private Color currentColor;

    public MenuButton(String text, boolean isEnabledByDefault, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        isEnabled = isEnabledByDefault;
    }

    public void setFontMetrics(FontMetrics fontMetrics) {
        stringWidth = fontMetrics.stringWidth(text);
        stringX = (x + (width / 2)) - (stringWidth / 2);
        stringY = y + (height / 2) + (font.getSize() / 4);
        setFontMetrics = true;
    }

    /**
     * Should be called whenever the user clicks/releases the mouse
     */
    public void checkClick(MouseEvent e) {
        if (isClickInBounds(e.getX(), e.getY()) && isEnabled) {
            onClick();
        }
    }

    /**
     * Called when the button is clicked and enabled
     */
    public void onClick() {

    }

    public void render(Graphics graphics) {
        if (!setFontMetrics) {
            setFontMetrics(graphics.getFontMetrics(font));
        }

        // background
        if (isEnabled) {
            graphics.setColor(currentColor);
        } else {
            graphics.setColor(disabledColor);
        }
        // graphics.fillRect(x, y, width, height);

        // text
        graphics.setFont(font);
        graphics.setColor(textColor);
        graphics.drawString(text, stringX, stringY);
    }

    public void tick() {
        // emulate hover effect
        if (isClickInBounds(Mouse.x, Mouse.y) && isEnabled) {
            currentColor = hoverColor;
        } else {
            currentColor = enabledColor;
        }
    }

    private boolean isClickInBounds(int cx, int cy) {
        return Util.isWithinBounds(cx, cy, x, y, width, height);
    }

}
