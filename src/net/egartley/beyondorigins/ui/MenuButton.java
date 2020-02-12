package net.egartley.beyondorigins.ui;

import java.awt.*;

public class MenuButton extends GenericButton {

    /**
     * Whether or not font metrics (string width and location) have been calculated. This is meant to avoid calculating the same thing every tick
     */
    public boolean setFontMetrics;
    private int stringX;
    private int stringY;

    public static Font font = new Font("Georgia", Font.BOLD, 24);

    /**
     * String that is displayed on the button
     */
    private String text;
    private Color disabledColor = Color.DARK_GRAY;
    private Color enabledColor = Color.RED.darker();
    private Color hoverColor = enabledColor.brighter();
    private Color textColor = new Color(0x0092ff);
    private Color hoverTextColor = new Color(0x00448f);
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

    public void setFontMetrics(FontMetrics fontMetrics) {
        stringX = (x() + (width / 2)) - (fontMetrics.stringWidth(text) / 2);
        stringY = y() + (height / 2) + (font.getSize() / 4);
        setFontMetrics = true;
    }

    @Override
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
