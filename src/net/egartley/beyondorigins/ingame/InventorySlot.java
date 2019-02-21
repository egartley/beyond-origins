package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;

import java.awt.*;

public class InventorySlot {

    public static final int BASE_X = 311, BASE_Y = 167, MARGIN = 3, SIZE = 32;

    public int row, column, x, y, baseItemX, baseItemY;
    public boolean isEmpty;
    public InventoryItem item;

    public InventorySlot(int row, int column) {
        this(null, row, column);
    }

    public InventorySlot(InventoryItem item, int row, int column) {
        putItem(item);
        this.row = row;
        this.column = column;
        // calculate in constructor instead of calculating the same thing every tick
        x = column * (SIZE + MARGIN) + BASE_X;
        y = row * (SIZE + MARGIN) + BASE_Y;
        baseItemX = x;
        baseItemY = y;
    }

    public void putItem(InventoryItem item) {
        this.item = item;
        if (item != null) {
            item.slot = this;
        }
        isEmpty = item == null;
    }

    private InventoryItem replaceItem(InventoryItem toReplaceWith) {
        InventoryItem old = item;
        putItem(toReplaceWith);
        return old;
    }

    public InventoryItem removeItem() {
        return replaceItem(null);
    }

    public InventoryItem swapItem(InventoryItem toSwap) {
        return replaceItem(toSwap);
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.GRAY);
        graphics.fillRect(x, y, SIZE, SIZE);
        if (Game.debug) {
            graphics.setColor(Color.WHITE);
            graphics.drawString(String.valueOf((row * Inventory.ROWS) + column), x + 3, y + 12);
        }
    }

    @Override
    public String toString() {
        return row + ", " + column;
    }

}
