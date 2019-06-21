package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.input.Mouse;

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
            slot.putItem(this);
        }
    }

    public void tick() {
        // TODO: change to sprite width and height
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
        graphics.setColor(Color.BLACK);
        graphics.fillOval(renderX, renderY, InventorySlot.SIZE, InventorySlot.SIZE);
    }

    public void drawToolTip(Graphics graphics) {
        if (!setFontMetrics) {
            tooltipWidth = graphics.getFontMetrics().stringWidth(name);
            setFontMetrics = true;
        }
        if ((mouseHover && Game.inGameState.inventory.getItemBeingDragged() == null) || isBeingDragged) {
            graphics.setColor(tooltipBackgroundColor);
            graphics.fillRect((renderX + InventorySlot.SIZE / 2) - (tooltipWidth / 2), renderY - 18, tooltipWidth, 16);
            graphics.setColor(Color.WHITE);
            graphics.drawString(name, (renderX + InventorySlot.SIZE / 2) - (tooltipWidth / 2), renderY - 6);
        }
    }

}
