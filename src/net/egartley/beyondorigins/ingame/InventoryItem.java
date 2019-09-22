package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.entities.DroppedItem;
import net.egartley.beyondorigins.ui.InventoryPanel;
import net.egartley.gamelib.abstracts.Renderable;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.interfaces.Tickable;

import java.awt.*;
import java.util.ArrayList;

public class InventoryItem extends Renderable implements Tickable {

    private static Font tooltipFont = new Font("Arial", Font.BOLD, 14);
    private static Color tooltipBorderColor = new Color(65, 11, 67);

    private int tooltipWidth;

    public boolean isBeingDragged, didStartDrag, mouseHover, setFontMetrics, isShowingTooltip;

    private Inventory inventory;

    public Item item;
    public InventorySlot slot;

    public InventoryItem(Inventory inventory, Item item, InventorySlot slot) {
        this(inventory, item, slot, false);
    }

    public InventoryItem(Inventory inventory, Item item, InventorySlot slot, boolean setToSlot) {
        this.inventory = inventory;
        this.item = item;
        this.slot = slot;
        setPosition(slot.baseItemX, slot.baseItemY);
        if (setToSlot) {
            slot.set(this);
        }
    }

    public void tick() {
        mouseHover = Util.isWithinBounds(Mouse.x, Mouse.y, x(), y(), InventorySlot.SIZE, InventorySlot.SIZE);
        isShowingTooltip = isBeingDragged || (mouseHover && inventory.itemBeingDragged == null);
        if (Mouse.isDragging) {
            if ((mouseHover || didStartDrag) && (inventory.itemBeingDragged == this || inventory.itemBeingDragged == null)) {
                inventory.itemBeingDragged = this;
                setPosition(Mouse.x - (InventorySlot.SIZE / 2), Mouse.y - (InventorySlot.SIZE / 2));
                didStartDrag = true;
                isBeingDragged = true;
            }
        } else if (isBeingDragged) {
            onDragEnd();
            inventory.itemBeingDragged = null;
            isBeingDragged = false;
        } else {
            setPosition(slot.baseItemX, slot.baseItemY);
            didStartDrag = false;
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(item.image, x(), y(), null);

        if (!setFontMetrics) {
            tooltipWidth = graphics.getFontMetrics(tooltipFont).stringWidth(item.name);
            setFontMetrics = true;
        }
    }

    public void drawToolTip(Graphics graphics) {
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(tooltipBorderColor);
        graphics.fillRoundRect(Mouse.x - 2, Mouse.y - 28, tooltipWidth + 14, 26, 8, 8);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(Mouse.x + 1, Mouse.y - 25, tooltipWidth + 8, 20);
        graphics.setColor(Color.WHITE);
        graphics.setFont(tooltipFont);
        graphics.drawString(item.name, Mouse.x + 5, Mouse.y - 10);
    }

    private void onDragEnd() {
        ArrayList<Rectangle> intersectionRectangles = new ArrayList<>();
        ArrayList<InventorySlot> intersectedSlots = new ArrayList<>();

        for (InventorySlot slot : inventory.slots) {
            Rectangle r1 = new Rectangle(slot.x(), slot.y(), InventorySlot.SIZE, InventorySlot.SIZE);
            Rectangle r2 = new Rectangle(x(), y(), InventorySlot.SIZE, InventorySlot.SIZE);
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
            if (closestSlot.isEmpty()) {
                // simply move the item
                this.move(closestSlot);
            } else {
                // swap with the item in the closest slot
                this.swap(closestSlot.item);
            }
        } else {
            // did not end over any slots
            InventoryPanel i = Game.in().inventory.inventoryPanel;
            if (!Util.isWithinBounds(x(), y(), i.x(), i.y(), i.width, i.height)) {
                drop();
            }
        }
    }

    /**
     * Moves this item to the specified slot
     *
     * @param moveTo The slot to move this item to
     */
    private void move(InventorySlot moveTo) {
        slot.removeItem();
        slot = moveTo;
        moveTo.set(this);
    }

    /**
     * Swaps places (slots) with the specified item
     *
     * @param swapWith The item to swap places with
     */
    private void swap(InventoryItem swapWith) {
        InventorySlot slot1 = this.slot;
        InventoryItem item1 = this;
        InventorySlot slot2 = swapWith.slot;
        InventoryItem item2 = swapWith;
        slot1.set(item2);
        slot2.set(item1);
    }

    /**
     * Removes this item from the inventory
     */
    private void selfDestruct() {
        slot.removeItem();
    }

    private void drop() {
        Game.in().map.sector.addEntity(new DroppedItem(item, Mouse.x - 8, Mouse.y - 8));
        selfDestruct();
    }

}
