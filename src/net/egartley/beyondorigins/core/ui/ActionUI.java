package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.core.abstracts.UIElement;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import org.newdawn.slick.*;

public class ActionUI extends UIElement {

    private int textWidth;
    private int fullWidth;
    private int playerMidX;
    private int textX;
    private final int PADDING = 6;

    private Color textColor = Color.white;
    private Color backgroundColor = new Color(0, 0, 0, 128);
    private Font font = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12), true);
    private Image icon;
    private String text;

    public ActionUI(String text, int keycode) {
        this.text = text;
        icon = Images.getImageFromPath("resources/images/ui/keys/" + keycode + ".png");
        textWidth = font.getWidth(text);
        fullWidth = textWidth + PADDING + icon.getWidth();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRoundRect(textX - 2, y - 18, textWidth + 5, 16, 5);
        graphics.setColor(textColor);
        graphics.setFont(font);
        graphics.drawString(text, textX, y - 16);
        graphics.drawImage(icon, textX + textWidth + PADDING, y - 19);
    }

    @Override
    public void tick() {
        y = Entities.PLAYER.y;
        playerMidX = Entities.PLAYER.x + (Entities.PLAYER.sprite.width / 2);
        textX = playerMidX - (fullWidth / 2);
    }

}
