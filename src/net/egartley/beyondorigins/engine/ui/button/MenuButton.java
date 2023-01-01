package net.egartley.beyondorigins.engine.ui.button;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class MenuButton extends GenericButton {

    private int stringX;
    private int stringY;
    private boolean didSetStringCoords;
    private Color color;
    private Color currentTextColor;
    private final String text;
    private final Color TEXT_COLOR = Color.black;
    private final Color DISABLED_COLOR = Color.darkGray;
    private final Color HOVER_TEXT_COLOR = Color.darkGray;
    private final Color ENABLED_COLOR = Color.red.darker();
    private final Color HOVER_COLOR = ENABLED_COLOR.brighter();
    private final Font FONT =
            new TrueTypeFont(new java.awt.Font("Georgia", java.awt.Font.PLAIN, 24), true);

    public MenuButton(String text, boolean isEnabledByDefault, int x, int y, int width, int height) {
        super(width, height, x, y);
        if (isEnabledByDefault) {
            isEnabled = true;
        }
        this.text = text;
    }

    private void setStringCoords(Font font) {
        stringX = x + (width / 2) - (font.getWidth(text) / 2);
        stringY = y + (height / 2) - (font.getHeight(text) / 2);
        didSetStringCoords = true;
    }

    @Override
    public void tick() {
        super.tick();
        color = isHovering ? HOVER_COLOR : ENABLED_COLOR;
        currentTextColor = isHovering ? HOVER_TEXT_COLOR : TEXT_COLOR;
    }

    @Override
    public void render(Graphics graphics) {
        if (!didSetStringCoords) {
            setStringCoords(FONT);
        }
        graphics.setColor(isEnabled ? color : DISABLED_COLOR);
        graphics.setFont(FONT);
        graphics.setColor(currentTextColor);
        graphics.drawString(text, stringX, stringY);
    }

}
