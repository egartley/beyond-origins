package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.abstracts.Renderable;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.interfaces.Tickable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InventorySlot extends Renderable implements Tickable {

    public static final int MARGIN = 3, SIZE = 36;

    public static BufferedImage image = ImageStore.get("resources/images/ui/inventory-slot.png");

    public int row, column, baseItemX, baseItemY;
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
        x((column * (SIZE + MARGIN)) + ((Game.WINDOW_WIDTH / 2) - (temp.width / 2)) + 24);
        y((row * (SIZE + MARGIN)) + ((Game.WINDOW_HEIGHT / 2) - ((Inventory.ROWS * (SIZE + MARGIN)) / 2)));
        baseItemX = x() + 2;
        baseItemY = y() + 2;
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

    @Override
    public void tick() {
        if (item != null) {
            item.tick();
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x(), y(), null);
    }

    @Override
    public String toString() {
        return row + ", " + column;
    }

}
