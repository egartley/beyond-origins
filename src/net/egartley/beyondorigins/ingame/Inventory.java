package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.graphics.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

import java.awt.*;
import java.util.ArrayList;

public class Inventory extends StaticEntity {

    public static final int ROWS = 6, COLUMNS = 6;

    public ArrayList<InventorySlot> slots;
    public ArrayList<InventoryItem> items;

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

        items.add(new InventoryItem("Test Item", slots.get(3), null, true));
        items.add(new InventoryItem("Another One", slots.get(getSlotIndexFromRowColumn(3, 3)), null, true));
    }

    void onItemDragEnd(InventoryItem dropItem) {

        // TODO: change r2 width and height to item's sprite width and height

        ArrayList<Rectangle> intersectionRectangles = new ArrayList<>();
        ArrayList<InventorySlot> intersectedSlots = new ArrayList<>();
        InventorySlot originalSlot = dropItem.slot;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                InventorySlot slot = slots.get(getSlotIndexFromRowColumn(r, c));
                Rectangle r1 = new Rectangle(slot.x, slot.y, InventorySlot.SIZE, InventorySlot.SIZE);
                Rectangle r2 = new Rectangle(dropItem.renderX, dropItem.renderY, InventorySlot.SIZE, InventorySlot.SIZE);
                if (r1.intersects(r2)) {
                    if (slot.isEmpty) {
                        intersectionRectangles.add(r1.intersection(r2));
                        intersectedSlots.add(slot);
                    }
                    if (intersectionRectangles.size() == 4) {
                        // item can only be within bounds of up to four slots
                        break;
                    }
                }
            }
        }

        if (!intersectionRectangles.isEmpty()) {
            // at least one slot
            // i is the index of the closest intersection, n is a counter
            int i = 0, n = 0;
            Rectangle closest = intersectionRectangles.get(0);
            // find the "biggest" intersection by each rectangle's area
            for (Rectangle r : intersectionRectangles) {
                if ((r.width * r.height) > (closest.width * closest.height)) {
                    closest = r;
                    i = n;
                }
                n++;
            }
            // now actually move the item to the slot it is closest to
            intersectedSlots.get(i).putItem(dropItem);
            originalSlot.removeItem();
        }
        // else, there were no intersections with any slots, so it will move back
    }

    public InventoryItem getItemBeingDragged() {
        for (InventoryItem i : items)
            if (i.isBeingDragged)
                return i;
        return null;
    }

    private int getSlotIndexFromRowColumn(int row, int column) {
        return row * ROWS + column;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);

        graphics.drawImage(sprite.toBufferedImage(), 289, 145, null);

        for (InventorySlot s : slots)
            s.render(graphics);
        for (InventoryItem i : items)
            i.render(graphics);
        for (InventoryItem i : items)
            i.drawToolTip(graphics);
    }

    @Override
    public void tick() {
        for (InventoryItem i : items)
            i.tick();
    }

    @Override
    protected void setBoundaries() {

    }

}
