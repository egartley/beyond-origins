package net.egartley.beyondorigins.ingame;

import java.awt.*;

public class InventorySlot {

    public static final int BASE_X = 311, BASE_Y = 167, MARGIN = 3, SIZE = 32;

    public int row, column, x, y;
    public GameItem item;

    public InventorySlot(int row, int column) {
        this(null, row, column);
    }

    public InventorySlot(GameItem item, int row, int column) {
        this.item = item;
        this.row = row;
        this.column = column;
        // calculate in constructor instead of calculating the same thing every tick
        x = (row * (SIZE + MARGIN)) + BASE_X;
        y = (column * (SIZE + MARGIN)) + BASE_Y;
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.GRAY);
        graphics.fillRect(x, y, 32, 32);
        if (item != null)
            item.render(graphics);
    }

}
