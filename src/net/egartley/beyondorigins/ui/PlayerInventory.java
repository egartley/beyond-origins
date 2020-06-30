package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.PlayerInventorySlot;
import net.egartley.beyondorigins.ingame.PlayerInventoryStack;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.logic.inventory.ItemStack;
import net.egartley.gamelib.logic.math.Calculate;
import org.newdawn.slick.*;

import java.util.ArrayList;

public class PlayerInventory extends UIElement {

    public static final int ROWS = 5, COLUMNS = 4;

    private int detailsLineIndex = 0;

    private static final Color tooltipBorderColor = new Color(65, 11, 67);

    public static int tooltipWidth;
    public static boolean isShowingTooltip;
    public static String tooltipText;
    public static Font tooltipFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14), true);
    public static PlayerInventoryStack stackBeingDragged;
    public static ArrayList<PlayerInventorySlot> slots = new ArrayList<>();

    private static final Font detailsFont = new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 11), true);
    private static final Font playerNameFont = new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 14), true);
    private static final Color playerNameColor = new Color(65, 53, 37);
    private static final Color detailsColor = new Color(111, 88, 61);

    public PlayerInventory() {
        super(Images.get(Images.INVENTORY_PANEL));
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
        // panel (background)
        graphics.drawImage(image, x(), y());

        // slots
        slots.forEach(slot -> slot.render(graphics));
        slots.forEach(slot -> slot.renderStack(graphics));

        // player display
        Image playerImage = Entities.PLAYER.sprite.asImage();
        graphics.drawImage(playerImage, Calculate.getCenter(x() + 243, playerImage.getWidth()), Calculate.getCenter(y() + 96, playerImage.getHeight()));

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
        graphics.setColor(tooltipBorderColor);
        graphics.fillRoundRect(Mouse.x - 2, Mouse.y - 28, tooltipWidth + 14, 26, 8, 8);
        graphics.setColor(Color.black);
        graphics.fillRect(Mouse.x + 1, Mouse.y - 25, tooltipWidth + 8, 20);
        graphics.setColor(Color.white);
        graphics.setFont(tooltipFont);
        graphics.drawString(tooltipText, Mouse.x + 5, Mouse.y - 10);
    }

    public static void stackHover() {
        isShowingTooltip = true;
    }

    public static void populate() {
        for (int i = 0; i < slots.size(); i++) {
            System.out.println(i);
            PlayerInventorySlot slot = slots.get(i);
            ItemStack itemStack = Entities.PLAYER.inventory.getStack(i);
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
