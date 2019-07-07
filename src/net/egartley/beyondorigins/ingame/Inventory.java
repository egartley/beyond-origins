package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.objects.StaticEntity;

import java.awt.*;
import java.util.ArrayList;

public class Inventory extends StaticEntity {

    public static final int ROWS = 5, COLUMNS = 4;

    public ArrayList<InventorySlot> slots;
    public ArrayList<InventoryItem> items;

    private Color backgroundColor = new Color(0, 0, 0, 152);

    public Inventory(Sprite sprite) {
        super("Inventory", sprite);
        slots = new ArrayList<>();
        items = new ArrayList<>();
        x = (Game.WINDOW_WIDTH / 2) - (sprite.width / 2);
        y = (Game.WINDOW_HEIGHT / 2) - (sprite.height / 2);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                slots.add(new InventorySlot(r, c));
            }
        }

        // temp
        items.add(new InventoryItem("Feels Bad Man", slots.get(0), Util.rotateImage(ImageStore.get("resources/images/items/wojak.png"), Math.PI), true));
        items.add(new InventoryItem("Wojak", slots.get(1), ImageStore.get("resources/images/items/wojak.png"), true));
    }

    void onItemDragEnd(InventoryItem dropItem) {

        // TODO: change r2 width and height to item's sprite width and height

        ArrayList<Rectangle> intersectionRectangles = new ArrayList<>();
        ArrayList<InventorySlot> intersectedSlots = new ArrayList<>();
        InventorySlot originalSlot = dropItem.slot;

        for (InventorySlot slot : slots) {
            Rectangle r1 = new Rectangle(slot.x, slot.y, InventorySlot.SIZE, InventorySlot.SIZE);
            Rectangle r2 = new Rectangle(dropItem.renderX, dropItem.renderY, InventorySlot.SIZE, InventorySlot.SIZE);
            if (r1.intersects(r2)) {
                intersectionRectangles.add(r1.intersection(r2));
                intersectedSlots.add(slot);
                if (intersectionRectangles.size() == 4) {
                    // item can only be within bounds of up to four slots
                    break;
                }
            }
        }

        if (!intersectionRectangles.isEmpty()) {
            // at least one slot
            int i = 0, n = 0;
            Rectangle closest = intersectionRectangles.get(0);
            // find the slot the item is closet to
            for (Rectangle r : intersectionRectangles) {
                if ((r.width * r.height) > (closest.width * closest.height)) {
                    closest = r;
                    i = n;
                }
                n++;
            }
            // move the item to the slot it is closest to
            InventorySlot closestSlot = intersectedSlots.get(i);
            if (closestSlot.isEmpty) {
                // simply move the item
                dropItem.move(closestSlot);
            } else {
                // swap with the item in the closest slot
                dropItem.swap(closestSlot.item);
            }
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

        graphics.drawImage(sprite.toBufferedImage(), (int) x, (int) y, null);

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
