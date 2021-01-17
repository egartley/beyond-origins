package net.egartley.beyondorigins.core.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class MenuButton extends GenericButton {

    private int stringX;
    private int stringY;
    private boolean didSetStringCoords;
    private final String text;
    private final Color textColor = Color.black;
    private final Color disabledColor = Color.darkGray;
    private final Color hoverTextColor = Color.darkGray;
    private final Color enabledColor = Color.red.darker();
    private final Color hoverColor = enabledColor.brighter();

    public static Font font = new TrueTypeFont(new java.awt.Font("Georgia", java.awt.Font.PLAIN, 24), true);
    private Color color;
    private Color currentTextColor;

    public MenuButton(String text, boolean isEnabledByDefault, int x, int y, int width, int height) {
        super(width, height, x, y);
        if (isEnabledByDefault) {
            isEnabled = true;
        }
        this.text = text;
    }

    @Override
    public void render(Graphics graphics) {
        if (!didSetStringCoords) {
            setStringCoords(font);
        }
        graphics.setColor(isEnabled ? color : disabledColor);
        graphics.setFont(font);
        graphics.setColor(currentTextColor);
        graphics.drawString(text, stringX, stringY);
    }

    @Override
    public void tick() {
        super.tick();
        color = isBeingHovered ? hoverColor : enabledColor;
        currentTextColor = isBeingHovered ? hoverTextColor : textColor;
    }

    private void setStringCoords(Font font) {
        stringX = x() + (width / 2) - (font.getWidth(text) / 2);
        stringY = y() + (height / 2) - (font.getHeight(text) / 2);
        didSetStringCoords = true;
    }

}
