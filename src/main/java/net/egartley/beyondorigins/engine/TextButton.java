package net.egartley.beyondorigins.engine;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class TextButton extends Button {

    private int stringX, stringY;

    private String text;
    private Color textColor;
    private static final Font FONT = new TrueTypeFont(new java.awt.Font("Georgia", java.awt.Font.PLAIN, 24), true);

    public TextButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
        setPosition(x, y);
    }

    @Override
    public void tick() {
        super.tick();
        textColor = isHovering ? Color.darkGray : Color.white;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(textColor);
        graphics.setFont(FONT);
        graphics.drawString(text, stringX, stringY);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        stringX = x + (width / 2) - (FONT.getWidth(text) / 2);
        stringY = y + (height / 2) - (FONT.getHeight(text) / 2);
    }

    public void setText(String newText) {
        text = newText;
    }

    public String getText() {
        return text;
    }

}
