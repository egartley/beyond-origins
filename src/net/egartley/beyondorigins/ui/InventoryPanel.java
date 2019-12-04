package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.InventorySlot;
import net.egartley.gamelib.logic.inventory.ItemStack;
import net.egartley.gamelib.logic.math.Calculate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class InventoryPanel extends UIElement {

    public static final int ROWS = 5, COLUMNS = 4;

    private int detailsLineIndex = 0;
    private boolean gotFontMetrics;

    public static ArrayList<InventorySlot> slots = new ArrayList<>();

    public static ItemStack stackBeingDragged;

    private static Font detailsFont = new Font("Bookman Old Style", Font.BOLD, 11);
    private static Font playerNameFont = new Font("Bookman Old Style", Font.BOLD, 14);
    private static Color playerNameColor = new Color(65, 53, 37);
    private static Color detailsColor = new Color(111, 88, 61);
    private static FontMetrics fontMetrics;

    public InventoryPanel() {
        super(ImageStore.get(ImageStore.INVENTORY_PANEL));
        setPosition(Calculate.getCenteredX(width), Calculate.getCenteredY(height));
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                slots.add(new InventorySlot(null, r, c));
            }
        }
    }

    private void renderLine(String text, Graphics graphics) {
        graphics.drawString(text, x() + 282, y() + 91 + (detailsLineIndex * 14));
        detailsLineIndex++;
    }

    @Override
    public void tick() {
        slots.forEach(slot -> slot.set(Entities.PLAYER.inventory.get(slot.index)));
    }

    @Override
    public void render(Graphics graphics) {
        if (!gotFontMetrics) {
            fontMetrics = graphics.getFontMetrics(detailsFont);
            gotFontMetrics = true;
        }
        // panel (background)
        graphics.drawImage(image, x(), y(), null);

        // slots
        slots.forEach(slot -> slot.render(graphics));
        slots.forEach(slot -> slot.renderStack(graphics));
        if (stackBeingDragged != null) {
            stackBeingDragged.render(graphics);
        }

        // player display
        BufferedImage playerImage = Entities.PLAYER.sprite.toBufferedImage();
        graphics.drawImage(playerImage, Calculate.getCenter(x() + 243, playerImage.getWidth()), Calculate.getCenter(y() + 96, playerImage.getHeight()), null);

        // details text
        graphics.setColor(playerNameColor);
        graphics.setFont(playerNameFont);
        graphics.drawString("Player's Name", x() + 282, y() + 73);
        graphics.setColor(detailsColor);
        graphics.setFont(detailsFont);
        renderLine("Level " + Entities.PLAYER.getLevel(), graphics);
        renderLine("EXP: " + Entities.PLAYER.getExperience(), graphics);
        detailsLineIndex = 0;
    }

}
