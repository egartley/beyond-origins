package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Util;
import net.egartley.gamelib.input.Mouse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class InventoryItem {

    private static Font tooltipFont = new Font("Arial", Font.PLAIN, 11);

    public int renderX, renderY, tooltipWidth;
    public boolean isBeingDragged, didStartDrag, mouseHover, setFontMetrics;
    public String name;
    public BufferedImage image;
    public InventorySlot slot;

    public InventoryItem(String name, InventorySlot slot, BufferedImage image) {
        this(name, slot, image, false);
    }

    public InventoryItem(String name, InventorySlot slot, BufferedImage image, boolean setToSlot) {
        this.name = name;
        this.slot = slot;
        this.image = image;
        renderX = slot.baseItemX;
        renderY = slot.baseItemY;
        if (setToSlot) {
            slot.putItem(this);
        }
    }

    public void tick() {
        mouseHover = Util.isWithinBounds(Mouse.x, Mouse.y, renderX, renderY, InventorySlot.SIZE, InventorySlot.SIZE);
        if (Mouse.isDragging) {
            if ((mouseHover || didStartDrag) && (Inventory.getItemBeingDragged() == this || Inventory.getItemBeingDragged() == null)) {
                renderX = Mouse.x - (InventorySlot.SIZE / 2);
                renderY = Mouse.y - (InventorySlot.SIZE / 2);
                didStartDrag = true;
                isBeingDragged = true;
            }
        } else if (isBeingDragged) {
            onDragEnd();
            isBeingDragged = false;
        } else {
            renderX = slot.baseItemX;
            renderY = slot.baseItemY;
            didStartDrag = false;
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(image, renderX, renderY, null);
    }

    void drawToolTip(Graphics graphics) {
        if (!setFontMetrics) {
            tooltipWidth = graphics.getFontMetrics(tooltipFont).stringWidth(name);
            setFontMetrics = true;
        }
        if ((mouseHover && Inventory.getItemBeingDragged() == null) || isBeingDragged) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect((renderX + InventorySlot.SIZE / 2) - (tooltipWidth / 2), renderY - 18, tooltipWidth, 16);
            graphics.setColor(Color.WHITE);
            graphics.setFont(tooltipFont);
            graphics.drawString(name, (renderX + InventorySlot.SIZE / 2) - (tooltipWidth / 2), renderY - 6);
        }
    }

    private void onDragEnd() {
        ArrayList<Rectangle> intersectionRectangles = new ArrayList<>();
        ArrayList<InventorySlot> intersectedSlots = new ArrayList<>();

        for (InventorySlot slot : Inventory.slots) {
            Rectangle r1 = new Rectangle(slot.x, slot.y, InventorySlot.SIZE, InventorySlot.SIZE);
            Rectangle r2 = new Rectangle(this.renderX, this.renderY, InventorySlot.SIZE, InventorySlot.SIZE);
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
                this.move(closestSlot);
            } else {
                // swap with the item in the closest slot
                this.swap(closestSlot.item);
            }
        }
        // else, there were no intersections with any slots, so it will move back
    }

    void move(InventorySlot moveTo) {
        slot.removeItem();
        slot = moveTo;
        moveTo.putItem(this);
    }

    void swap(InventoryItem swapWith) {
        InventorySlot slot1 = this.slot;
        InventoryItem item1 = this;
        InventorySlot slot2 = swapWith.slot;
        InventoryItem item2 = swapWith;
        slot1.putItem(item2);
        slot2.putItem(item1);
    }

}
