package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.gamelib.graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InventorySlot {

    public static final int MARGIN = 3, SIZE = 36;

    private BufferedImage image;

    public int row, column, x, y, baseItemX, baseItemY;
    public boolean isEmpty = true;
    public InventoryItem item;

    public InventorySlot(int row, int column) {
        this(null, row, column);
    }

    public InventorySlot(InventoryItem item, int row, int column) {
        this.row = row;
        this.column = column;
        putItem(item);
        Sprite temp = Entities.getTemplate(Entities.TEMPLATE_INVENTORY);
        x = (column * (SIZE + MARGIN)) + ((Game.WINDOW_WIDTH / 2) - (temp.width / 2)) + 24;
        y = (row * (SIZE + MARGIN)) + ((Game.WINDOW_HEIGHT / 2) - ((Inventory.ROWS * (SIZE + MARGIN)) / 2));
        baseItemX = x + 2;
        baseItemY = y + 2;

        image = ImageStore.get("resources/images/ui/inventory-slot.png");
    }

    InventoryItem putItem(InventoryItem item) {
        InventoryItem existing = this.item;
        this.item = item;
        if (item != null) {
            // set the item's slot to this
            item.slot = this;
        }
        isEmpty = item == null;
        return existing;
    }

    /**
     * Removes/deletes the slot's current item
     *
     * @return The item that was removed/deleted
     */
    InventoryItem removeItem() {
        return putItem(null);
    }

    public void render(Graphics graphics) {
        graphics.drawImage(image, x, y, null);
        /*if (Game.debug) {
            graphics.setColor(Color.WHITE);
            graphics.drawString(String.valueOf((row * Inventory.ROWS) + column), x + 3, y + 12);
        }*/
    }

    @Override
    public String toString() {
        return row + ", " + column;
    }

}
