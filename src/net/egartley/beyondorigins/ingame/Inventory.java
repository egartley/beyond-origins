package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.objects.StaticEntity;

import java.awt.*;
import java.util.ArrayList;

public class Inventory extends StaticEntity {

    public static final int ROWS = 5, COLUMNS = 4;

    public InventoryItem itemBeingDragged;
    public ArrayList<InventorySlot> slots = new ArrayList<>();

    private Color backgroundColor = new Color(0, 0, 0, 152);

    public Inventory(Sprite sprite) {
        super("Inventory", sprite);
        setPosition((Game.WINDOW_WIDTH / 2) - (sprite.width / 2), (Game.WINDOW_HEIGHT / 2) - (sprite.height / 2));

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                slots.add(new InventorySlot(r, c));
            }
        }
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
     * Puts the item in the next available slot, does nothing if full
     *
     * @param item The item to put in the inventory
     * @param amount How many of that item to put
     *
     * @return Whether or not the item was successfully put into the inventory
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

    public boolean isFull() {
        for (InventorySlot slot : slots) {
            if (slot.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private int getSlotIndexFromRowColumn(int row, int column) {
        return row * ROWS + column;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);

        graphics.drawImage(sprite.toBufferedImage(), x(), y(), null);

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

    @Override
    public void tick() {
        slots.forEach(InventorySlot::tick);
    }

    @Override
    protected void setBoundaries() {

    }

    @Override
    protected void setCollisions() {

    }

}
