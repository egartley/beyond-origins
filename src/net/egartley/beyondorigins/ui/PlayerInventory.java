package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.PlayerInventorySlot;
import net.egartley.beyondorigins.ingame.PlayerInventoryStack;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.logic.inventory.ItemStack;
import net.egartley.gamelib.logic.math.Calculate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayerInventory extends UIElement {

    public static final int ROWS = 5, COLUMNS = 4;

    private int detailsLineIndex = 0;
    private boolean gotFontMetrics;

    private static Color tooltipBorderColor = new Color(65, 11, 67);

    public static int tooltipWidth;
    public static boolean isShowingTooltip;
    public static String tooltipText;
    public static Font tooltipFont = new Font("Arial", Font.BOLD, 14);
    public static PlayerInventoryStack stackBeingDragged;
    public static ArrayList<PlayerInventorySlot> slots = new ArrayList<>();

    private static Font detailsFont = new Font("Bookman Old Style", Font.BOLD, 11);
    private static Font playerNameFont = new Font("Bookman Old Style", Font.BOLD, 14);
    private static Color playerNameColor = new Color(65, 53, 37);
    private static Color detailsColor = new Color(111, 88, 61);
    private static FontMetrics fontMetrics;

    public PlayerInventory() {
        super(ImageStore.get(ImageStore.INVENTORY_PANEL));
        setPosition(Calculate.getCenteredX(width), Calculate.getCenteredY(height));
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                slots.add(new PlayerInventorySlot(null, r, c));
            }
        }
    }

    private void renderLine(String text, Graphics graphics) {
        graphics.drawString(text, x() + 282, y() + 91 + (detailsLineIndex * 14));
        detailsLineIndex++;
    }

    @Override
    public void tick() {
        boolean nohover = true;
        for (PlayerInventorySlot slot : slots) {
            slot.tick();
            if (!slot.isEmpty() && slot.stack.mouseHover) {
                isShowingTooltip = true;
                nohover = false;
            }
        }
        if (nohover) {
            isShowingTooltip = false;
        }
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

        if (stackBeingDragged != null) {
            stackBeingDragged.render(graphics);
        }
        if (isShowingTooltip) {
            drawToolTip(graphics);
        }
    }

    private void drawToolTip(Graphics graphics) {
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(tooltipBorderColor);
        graphics.fillRoundRect(Mouse.x - 2, Mouse.y - 28, tooltipWidth + 14, 26, 8, 8);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(Mouse.x + 1, Mouse.y - 25, tooltipWidth + 8, 20);
        graphics.setColor(Color.WHITE);
        graphics.setFont(tooltipFont);
        graphics.drawString(tooltipText, Mouse.x + 5, Mouse.y - 10);
    }

    public static void stackHover() {
        isShowingTooltip = true;
    }

    public static void populate() {
        for (PlayerInventorySlot slot : slots) {
            ItemStack itemStack = Entities.PLAYER.inventory.get(slots.indexOf(slot));
            if (itemStack != null) {
                slot.stack = new PlayerInventoryStack(itemStack, slot);
            } else {
                slot.stack = null;
            }
        }
    }

    public static void clearSlot(int index) {
        Entities.PLAYER.inventory.set(null, index);
    }

    public static void swapStacks(PlayerInventoryStack stack1, PlayerInventoryStack stack2) {
        ItemStack items1 = stack1.itemStack;
        ItemStack items2 = stack2.itemStack;
        stack1.itemStack = items2;
        stack2.itemStack = items1;
        Entities.PLAYER.inventory.set(items1, stack2.slot.index);
        Entities.PLAYER.inventory.set(items2, stack1.slot.index);
    }

    public static void moveStackToEmpty(PlayerInventoryStack stack, int emptySlotIndex) {
        clearSlot(stack.slot.index);
        Entities.PLAYER.inventory.set(stack.itemStack, emptySlotIndex);
        stack.slot.clear();
    }

}
