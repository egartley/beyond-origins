package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.logic.math.Calculate;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InventoryPanel extends UIElement {

    private int detailsLineIndex = 0;
    private boolean gotFontMetrics;

    private static Font detailsFont = new Font("Bookman Old Style", Font.BOLD, 11);
    private static Font playerNameFont = new Font("Bookman Old Style", Font.BOLD, 14);
    private static Color playerNameColor = new Color(65, 53, 37);
    private static Color detailsColor = new Color(111, 88, 61);
    private static FontMetrics fontMetrics;

    public InventoryPanel() {
        super(ImageStore.get(ImageStore.INVENTORY_PANEL));
        setPosition(Calculate.getCenteredX(width), Calculate.getCenteredY(height));
    }

    private void renderLine(String text, Graphics graphics) {
        graphics.drawString(text, x() + 282, y() + 91 + (detailsLineIndex * 14));
        detailsLineIndex++;
    }

    @Override
    public void render(Graphics graphics) {
        if (!gotFontMetrics) {
            fontMetrics = graphics.getFontMetrics(detailsFont);
            gotFontMetrics = true;
        }
        // panel (background)
        graphics.drawImage(image, x(), y(), null);
        // player display
        BufferedImage playerImage = Entities.PLAYER.sprite.toBufferedImage();
        graphics.drawImage(playerImage, Calculate.getCenter(x() + 243, playerImage.getWidth()), Calculate.getCenter(y() + 96, playerImage.getHeight()), null);
        // details text
        graphics.setColor(playerNameColor);
        graphics.setFont(playerNameFont);
        graphics.drawString("Player's Name", x() + 282, y() + 73);

        graphics.setColor(detailsColor);
        graphics.setFont(detailsFont);
        renderLine("Level: NaN", graphics);
        renderLine("Experience: NaN", graphics);
        renderLine("Wallet: Empty", graphics);
        renderLine("Equipment: None", graphics);

        detailsLineIndex = 0;
    }

}
