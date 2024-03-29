package net.egartley.beyondorigins.engine.ui;

import net.egartley.beyondorigins.engine.input.Mouse;
import net.egartley.beyondorigins.engine.logic.Calculate;
import net.egartley.beyondorigins.engine.logic.inventory.ItemStack;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.PlayerInventorySlot;
import net.egartley.beyondorigins.ingame.PlayerInventoryStack;
import org.newdawn.slick.*;

import java.util.ArrayList;

public class PlayerInventory extends UIElement {

    private int detailsLineIndex = 0;
    private static final Color DETAILS_COLOR = new Color(111, 88, 61);
    private static final Color PLAYER_NAME_COLOR = new Color(65, 53, 37);
    private static final Color TOOLTIP_BORDER_COLOR = new Color(65, 11, 67);
    private static final Font DETAILS_FONT =
            new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 11), true);
    private static final Font PLAYER_NAME_FONT =
            new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.BOLD, 14), true);

    public static int tooltipWidth;
    public static boolean isShowingTooltip;
    public static final int ROWS = 5;
    public static final int COLUMNS = 4;
    public static String tooltipText;
    public static PlayerInventoryStack stackBeingDragged;
    public static ArrayList<PlayerInventorySlot> slots = new ArrayList<>();
    public static final Font TOOLTIP_FONT =
            new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14), true);

    public PlayerInventory() {
        super(Images.getImage(Images.INVENTORY_PANEL));
        this.x = Calculate.getCenteredX(width);
        this.y = Calculate.getCenteredY(height);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                slots.add(new PlayerInventorySlot(null, r, c));
            }
        }
    }

    private void renderLine(String text, Graphics graphics) {
        graphics.drawString(text, x + 282, y + 78 + (detailsLineIndex * PLAYER_NAME_FONT.getLineHeight()));
        detailsLineIndex++;
    }

    private void drawToolTip(Graphics graphics) {
        graphics.setColor(TOOLTIP_BORDER_COLOR);
        graphics.fillRoundRect(Mouse.x - 2, Mouse.y - 28, tooltipWidth + 14, 26, 8);
        graphics.setColor(Color.black);
        graphics.fillRoundRect(Mouse.x + 1, Mouse.y - 25, tooltipWidth + 8, 20, 6);
        graphics.setColor(Color.white);
        graphics.setFont(TOOLTIP_FONT);
        graphics.drawString(tooltipText, Mouse.x + 5, Mouse.y - 23);
    }

    public static void updateSlots() {
        for (int i = 0; i < slots.size(); i++) {
            PlayerInventorySlot slot = slots.get(i);
            ItemStack itemStack = Entities.PLAYER.inventory.getStack(i);
            slot.stack = itemStack != null ? new PlayerInventoryStack(itemStack, slot) : null;
        }
    }

    public static void clearSlot(int index) {
        Entities.PLAYER.inventory.setStackAt(null, index);
    }

    public static void swapStacks(PlayerInventoryStack stack1, PlayerInventoryStack stack2) {
        ItemStack items1 = stack1.itemStack;
        ItemStack items2 = stack2.itemStack;
        stack1.itemStack = items2;
        stack2.itemStack = items1;
        Entities.PLAYER.inventory.setStackAt(items1, stack2.slot.index);
        Entities.PLAYER.inventory.setStackAt(items2, stack1.slot.index);
    }

    public static void moveStackToEmpty(PlayerInventoryStack stack, int emptySlotIndex) {
        clearSlot(stack.slot.index);
        Entities.PLAYER.inventory.setStackAt(stack.itemStack, emptySlotIndex);
        stack.slot.clear();
    }

    @Override
    public void tick() {
        boolean isNotHovering = true;
        for (PlayerInventorySlot slot : slots) {
            slot.tick();
            if (!slot.isEmpty() && slot.stack.isBeingHovered) {
                isShowingTooltip = true;
                isNotHovering = false;
            }
        }
        isShowingTooltip = !isNotHovering;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x, y);
        slots.forEach(slot -> slot.render(graphics));
        slots.forEach(slot -> slot.renderStack(graphics));
        // player display
        Image playerImage = Entities.PLAYER.sprite.asImage();
        graphics.drawImage(playerImage,
                Calculate.getCenter(x + 243, playerImage.getWidth()),
                Calculate.getCenter(y + 96, playerImage.getHeight()));
        // details text
        graphics.setColor(PLAYER_NAME_COLOR);
        graphics.setFont(PLAYER_NAME_FONT);
        graphics.drawString("Player's Name", x + 282, y + 58);
        graphics.setColor(DETAILS_COLOR);
        graphics.setFont(DETAILS_FONT);
        renderLine("Level " + Entities.PLAYER.level, graphics);
        renderLine("Experience: " + Entities.PLAYER.experience, graphics);
        detailsLineIndex = 0;
        if (stackBeingDragged != null) {
            stackBeingDragged.render(graphics);
        }
        if (isShowingTooltip) {
            drawToolTip(graphics);
        }
    }

}
