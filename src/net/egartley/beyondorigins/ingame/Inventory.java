package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.ui.InventoryPanel;
import net.egartley.beyondorigins.ui.QuestsPanel;
import net.egartley.beyondorigins.ui.UIElement;
import net.egartley.gamelib.interfaces.Tickable;

import java.awt.*;
import java.util.ArrayList;

public class Inventory implements Tickable {

    static final int ROWS = 5, COLUMNS = 4;

    public UIElement panel;
    public InventoryItem itemBeingDragged;
    public InventoryPanel inventoryPanel;
    public QuestsPanel questsPanel;
    public ArrayList<InventorySlot> slots = new ArrayList<>();

    private Color backgroundColor = new Color(0, 0, 0, 152);

    public Inventory() {
        inventoryPanel = new InventoryPanel();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                slots.add(new InventorySlot(r, c));
            }
        }
        panel = inventoryPanel;

        questsPanel = new QuestsPanel();
    }

    /**
     * Puts the item in the next available slot, does nothing if full
     *
     * @param item The item to put in the inventory
     *
     * @return Whether or not the item was successfully put into the inventory
     */
    public boolean put(Item item) {
        return put(item, 1);
    }

    /**
     * Puts the item(s) in the next available slot, does nothing if full
     *
     * @param item The item to put in the inventory
     * @param amount How many of that item to put
     *
     * @return Whether or not the item(s) were successfully put into the inventory
     */
    public boolean put(Item item, int amount) {
        if (isFull()) {
            return false;
        }
        for (int i = 0; i < amount; i++) {
            for (InventorySlot slot : slots) {
                if (slot.isEmpty()) {
                    slot.set(new InventoryItem(this, item, slot, false));
                    break;
                }
            }
        }
        return true;
    }

    /**
     * @param item The item to check for
     * @return Whether or not the item is in the inventory
     */
    public boolean has(Item item) {
        for (InventorySlot slot : slots) {
            if (slot.item != null && slot.item.item.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the item from the inventory and returns it
     *
     * @param item The item to take
     * @return The item, or <code>null</code> if it wasn't there
     */
    public InventoryItem take(Item item) {
        for (InventorySlot slot : slots) {
            if (slot.item != null && slot.item.item.equals(item)) {
                return slot.removeItem();
            }
        }
        return null;
    }

    @Override
    public void tick() {
        slots.forEach(InventorySlot::tick);
    }

    public void render(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);

        panel.render(graphics);

        slots.forEach(slot -> slot.render(graphics));
        for (InventorySlot slot : slots) {
            if (slot.item != null && !slot.item.isBeingDragged) {
                slot.item.render(graphics);
            }
        }
        // make sure the item being dragged is rendered "above" all the other items, regardless of slot position
        if (itemBeingDragged != null) {
            itemBeingDragged.render(graphics);
        }
        for (InventorySlot slot : slots) {
            if (slot.item != null && slot.item.isShowingTooltip) {
                slot.item.drawToolTip(graphics);
            }
        }
    }

    /**
     * @return Whether or not the inventory is full
     */
    public boolean isFull() {
        for (InventorySlot slot : slots) {
            if (slot.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * "Converts" the given row and column to the index in {@link #slots}
     *
     * @param row    Row number
     * @param column Column number
     * @return The index in {@link #slots}
     */
    private int getSlotIndexFromRowColumn(int row, int column) {
        return row * ROWS + column;
    }

}
