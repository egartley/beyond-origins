package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.input.Mouse;

import java.awt.*;

public class InventorySlot {

    public static final int BASE_X = 311, BASE_Y = 167, MARGIN = 3, SIZE = 32;

    public int row, column, x, y, itemX, itemY, baseItemX, baseItemY;
    public boolean isBeingDragged;
    public GameItem item;

    public InventorySlot(int row, int column) {
        this(null, row, column);
    }

    public InventorySlot(GameItem item, int row, int column) {
        this.item = item;
        this.row = row;
        this.column = column;
        // calculate in constructor instead of calculating the same thing every tick
        x = column * (SIZE + MARGIN) + BASE_X;
        y = row * (SIZE + MARGIN) + BASE_Y;
        itemX = x;
        baseItemX = x;
        itemY = y;
        baseItemY = y;
    }

    public void tick() {
        if (item != null) {
            // TODO: change to item width and height
            if (Mouse.isDragging && Util.isWithinBounds(Mouse.x, Mouse.y, itemX, itemY, SIZE, SIZE)) {
                itemX = Mouse.x - (SIZE / 2);
                itemY = Mouse.y - (SIZE / 2);
                isBeingDragged = true;
            } else {
                itemX = baseItemX;
                itemY = baseItemY;
                if (isBeingDragged) {
                    Game.inGameState.inventory.onItemDragEnd(Mouse.x - (SIZE / 2), Mouse.y - (SIZE / 2), item);
                    isBeingDragged = false;
                }
            }
        }
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.GRAY);
        graphics.fillRect(x, y, SIZE, SIZE);
    }

}
