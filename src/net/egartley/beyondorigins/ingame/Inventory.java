package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
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

    void onItemDragEnd(int dropX, int dropY, GameItem item) {

        // TODO: move item to slot that's closest to item when it is within bounds of multiple slots

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                InventorySlot slot = slots.get(getSlotIndexFromRowColumn(r, c));
                Rectangle r1 = new Rectangle(slot.x, slot.y, InventorySlot.SIZE, InventorySlot.SIZE);
                // TODO: change r2 width and height to item's sprite width and height
                Rectangle r2 = new Rectangle(dropX, dropY, InventorySlot.SIZE, InventorySlot.SIZE);
                if (r1.intersects(r2) && !item.equals(slot.item)) {
                    item.slot = slot;
                    slot.item = item;
                    break;
                }
            }
        }
    }

    private int getSlotIndexFromRowColumn(int row, int column) {
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
