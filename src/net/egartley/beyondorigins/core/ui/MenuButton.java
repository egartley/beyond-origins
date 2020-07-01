package net.egartley.beyondorigins.core.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class MenuButton extends GenericButton {

    private int stringX;
    private int stringY;
    private boolean didSetStringCoords;

    public static Font font = new TrueTypeFont(new java.awt.Font("Georgia", java.awt.Font.PLAIN, 24), true);

    /**
     * String that is displayed on the button
     */
    private final String text;
    private final Color disabledColor = Color.darkGray;
    private final Color enabledColor = Color.red.darker();
    private final Color hoverColor = enabledColor.brighter();
    private final Color textColor = Color.black;
    private final Color hoverTextColor = Color.darkGray;
    /**
     * The color that is currently being used for the button
     */
    private Color currentColor;
    /**
     * The color that is currently being used for the text
     */
    private Color currentTextColor;

    public MenuButton(String text, boolean isEnabledByDefault, int x, int y, int width, int height) {
        super(width, height, x, y);
        if (isEnabledByDefault) {
            isEnabled = true;
        }
        this.text = text;
    }

    private void setStringCoords(Font font) {
        stringX = x() + (width / 2) - (font.getWidth(text) / 2);
        stringY = y() + (height / 2) - (font.getHeight(text) / 2);
        didSetStringCoords = true;
    }

    @Override
    public void render(Graphics graphics) {
        if (!didSetStringCoords) {
            setStringCoords(font);
        }

        // background
        if (isEnabled) {
            graphics.setColor(currentColor);
        } else {
            graphics.setColor(disabledColor);
        }
        // graphics.fillRect(x(), y(), width, height);

        // text
        graphics.setFont(font);
        graphics.setColor(currentTextColor);
        graphics.drawString(text, stringX, stringY);
    }

    @Override
    public void tick() {
        super.tick();
        // emulate hover effect
        if (isBeingHovered) {
            currentColor = hoverColor;
            currentTextColor = hoverTextColor;
        } else {
            currentColor = enabledColor;
            currentTextColor = textColor;
        }
    }

}
