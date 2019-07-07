package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.gamelib.input.Mouse;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InventoryItem {

    private static Font tooltipFont = new Font("Arial", Font.PLAIN, 11);

    public int renderX, renderY, tooltipWidth;
    public boolean isBeingDragged, didStartDrag, mouseHover, setFontMetrics;
    public String name;
    public BufferedImage image;
    public InventorySlot slot;

    public InventoryItem(String name, InventorySlot slot, BufferedImage image) {
        this(name, slot, image, false);
    }

    public InventoryItem(String name, InventorySlot slot, BufferedImage image, boolean setToSlot) {
        this.name = name;
        this.slot = slot;
        this.image = image;
        renderX = slot.baseItemX;
        renderY = slot.baseItemY;
        if (setToSlot) {
            slot.putItem(this);
        }
    }

    public void tick() {
        mouseHover = Util.isWithinBounds(Mouse.x, Mouse.y, renderX, renderY, InventorySlot.SIZE, InventorySlot.SIZE);
        if (Mouse.isDragging) {
            if ((mouseHover || didStartDrag) && (Game.inGameState.inventory.getItemBeingDragged() == this || Game.inGameState.inventory.getItemBeingDragged() == null)) {
                renderX = Mouse.x - (InventorySlot.SIZE / 2);
                renderY = Mouse.y - (InventorySlot.SIZE / 2);
                didStartDrag = true;
                isBeingDragged = true;
            }
        } else if (isBeingDragged) {
            Game.inGameState.inventory.onItemDragEnd(this);
            isBeingDragged = false;
        } else {
            renderX = slot.baseItemX;
            renderY = slot.baseItemY;
            didStartDrag = false;
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(image, renderX, renderY, null);
    }

    void drawToolTip(Graphics graphics) {
        if (!setFontMetrics) {
            tooltipWidth = graphics.getFontMetrics(tooltipFont).stringWidth(name);
            setFontMetrics = true;
        }
        if ((mouseHover && Game.inGameState.inventory.getItemBeingDragged() == null) || isBeingDragged) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect((renderX + InventorySlot.SIZE / 2) - (tooltipWidth / 2), renderY - 18, tooltipWidth, 16);
            graphics.setColor(Color.WHITE);
            graphics.setFont(tooltipFont);
            graphics.drawString(name, (renderX + InventorySlot.SIZE / 2) - (tooltipWidth / 2), renderY - 6);
        }
    }

    void move(InventorySlot moveTo) {
        slot.removeItem();
        slot = moveTo;
        moveTo.putItem(this);
    }

    void swap(InventoryItem swapWith) {
        InventorySlot slot1 = this.slot;
        InventoryItem item1 = this;
        InventorySlot slot2 = swapWith.slot;
        InventoryItem item2 = swapWith;
        slot1.putItem(item2);
        slot2.putItem(item1);
    }

}
