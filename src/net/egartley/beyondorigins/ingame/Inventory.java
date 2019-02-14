package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.graphics.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

import java.awt.*;
import java.util.ArrayList;

public class Inventory extends StaticEntity {

    public static final int ROWS = 6, COLUMNS = 6;

    public ArrayList<InventorySlot> slots;
    public ArrayList<GameItem> items;

    private Color backgroundColor = new Color(0, 0, 0, 152);

    public Inventory(Sprite sprite) {
        super("Inventory", sprite);
        slots = new ArrayList<>();
        items = new ArrayList<>();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                slots.add(new InventorySlot(r, c));
            }
        }

        items.add(new GameItem("Test Item", slots.get(3), null));
        slots.get(3).item = items.get(0);
    }

    public void onItemDragEnd(int dropX, int dropY, GameItem item) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                InventorySlot slot = slots.get(getSlotIndexFromRowColumn(r, c));
                Debug.out("Checking " + getSlotIndexFromRowColumn(r, c));
                if (Util.isWithinBounds(dropX, dropY, slot.x, slot.y, InventorySlot.SIZE, InventorySlot.SIZE) && !item.equals(slot.item)) {
                    item.slot = slot;
                    slot.item = item;
                    break;
                }
            }
        }
    }

    public int getSlotIndexFromRowColumn(int row, int column) {
        return (row * ROWS) + column;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);

        graphics.drawImage(sprite.toBufferedImage(), 289, 145, null);

        for (InventorySlot s : slots)
            s.render(graphics);
        for (GameItem i : items)
            i.render(graphics);
    }

    @Override
    public void tick() {
        for (InventorySlot s : slots)
            s.tick();
        // gameitem tick if needed
    }

    @Override
    protected void setBoundaries() {

    }

    @Override
    protected void setCollisions() {

    }
}
