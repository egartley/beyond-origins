package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.ui.UIElement;

public class InventorySlot extends UIElement {

    public static final int MARGIN = 3, SIZE = 36;

    int baseItemX;
    int baseItemY;
    public int row;
    public int column;
    public InventoryItem item;

    public InventorySlot(int row, int column) {
        this(null, row, column);
    }

    public InventorySlot(InventoryItem item, int row, int column) {
        super(ImageStore.get("resources/images/ui/inventory-slot.png"));
        this.row = row;
        this.column = column;
        set(item);
        x((column * (SIZE + MARGIN)) + ((Game.WINDOW_WIDTH / 2) - (ImageStore.get(ImageStore.INVENTORY_PANEL).getWidth() / 2)) + 24);
        y((row * (SIZE + MARGIN)) + (((Game.WINDOW_HEIGHT / 2) + 12) - ((PlayerMenu.ROWS * (SIZE + MARGIN)) / 2)));
        baseItemX = x() + 2;
        baseItemY = y() + 2;
    }

    /**
     * Sets the slot's item, and returns the item that was previously set (<code>null</code> if no item)
     *
     * @param item The item to put into the slot
     * @return The item that was previously in this slot, or <code>null</code> if there was nothing
     */
    InventoryItem set(InventoryItem item) {
        InventoryItem existing = this.item;
        this.item = item;
        if (item != null) {
            // set the item's slot to this
            item.slot = this;
        }
        return existing;
    }

    /**
     * Removes/deletes the slot's current item
     *
     * @return The item that was removed/deleted
     */
    InventoryItem removeItem() {
        return set(null);
    }

    @Override
    public void tick() {
        if (!isEmpty()) {
            item.tick();
        }
    }

    public boolean isEmpty() {
        return item == null;
    }

    @Override
    public String toString() {
        return row + ", " + column;
    }

}
