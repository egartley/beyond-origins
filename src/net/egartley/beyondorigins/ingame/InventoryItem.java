package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.graphics.Sprite;
import net.egartley.beyondorigins.input.Mouse;

import java.awt.*;

public class InventoryItem {

    private Color tooltipBackgroundColor = new Color(0, 0, 0, 128);

    public int renderX, renderY, tooltipWidth;
    public boolean isBeingDragged, didStartDrag, mouseHover, setFontMetrics;
    public String name;
    public Sprite sprite;
    public InventorySlot slot;

    public InventoryItem(String name, InventorySlot slot, Sprite sprite) {
        this(name, slot, sprite, false);
    }

    public InventoryItem(String name, InventorySlot slot, Sprite sprite, boolean setToSlot) {
        this.name = name;
        this.slot = slot;
        this.sprite = sprite;
        renderX = slot.baseItemX;
        renderY = slot.baseItemY;
        if (setToSlot) {
            this.slot.item = this;
        }
    }

    public void tick() {
        // TODO: change to sprite width and height
        mouseHover = Util.isWithinBounds(Mouse.x, Mouse.y, renderX, renderY, InventorySlot.SIZE, InventorySlot.SIZE);
        if (Mouse.isDragging) {
            if (mouseHover || didStartDrag) {
                renderX = Mouse.x - (InventorySlot.SIZE / 2);
                renderY = Mouse.y - (InventorySlot.SIZE / 2);
                didStartDrag = true;
            }
            isBeingDragged = true;
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
        if (!setFontMetrics) {
            tooltipWidth = graphics.getFontMetrics().stringWidth(name);
            setFontMetrics = true;
        }
        graphics.setColor(Color.BLACK);
        graphics.fillOval(renderX, renderY, InventorySlot.SIZE, InventorySlot.SIZE);
        if (mouseHover || isBeingDragged) {
            graphics.setColor(tooltipBackgroundColor);
            graphics.fillRect((renderX + InventorySlot.SIZE / 2) - (tooltipWidth / 2), renderY - 18, tooltipWidth, 16);
            graphics.setColor(Color.WHITE);
            graphics.drawString(name, (renderX + InventorySlot.SIZE / 2) - (tooltipWidth / 2), renderY - 6);
        }
    }

}
