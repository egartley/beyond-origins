package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.core.abstracts.UIElement;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import org.newdawn.slick.*;

public class ActionUI extends UIElement {

    private int textWidth;
    private int fullWidth;
    private int iconWidth;
    private int playerMidX;
    private int startX;
    private final int PADDING = 4;

    private Color textColor = Color.white;
    private Color backgroundColor = new Color(0, 0, 0, 128);
    private Font font = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12), true);
    private Image icon;
    private String text;

    public ActionUI(String text, int keycode) {
        this.text = text;
        icon = Images.getImageFromPath("resources/images/ui/keys/" + keycode + ".png");
        textWidth = font.getWidth(text);
        iconWidth = icon.getWidth();
        fullWidth = textWidth + PADDING + iconWidth;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(icon, startX, y - 19);
        graphics.setColor(backgroundColor);
        graphics.fillRoundRect(startX + iconWidth + PADDING, y - 18, textWidth + 5, 16, 5);
        graphics.setColor(textColor);
        graphics.setFont(font);
        graphics.drawString(text, startX + iconWidth + PADDING + 2, y - 16);
    }

    @Override
    public void tick() {
        y = Entities.PLAYER.y;
        playerMidX = Entities.PLAYER.x + (Entities.PLAYER.sprite.width / 2);
        startX = playerMidX - (fullWidth / 2);
    }

}
