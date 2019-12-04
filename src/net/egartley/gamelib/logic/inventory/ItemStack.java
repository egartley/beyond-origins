package net.egartley.gamelib.logic.inventory;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.entities.DroppedItem;
import net.egartley.beyondorigins.ingame.InventorySlot;
import net.egartley.beyondorigins.ui.InventoryPanel;
import net.egartley.gamelib.abstracts.GameItem;
import net.egartley.gamelib.abstracts.Renderable;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.interfaces.Tickable;

import java.awt.*;
import java.util.ArrayList;

public class ItemStack extends Renderable implements Tickable {

    public static int MAX_AMOUNT = 99;

    private static Font tooltipFont = new Font("Arial", Font.BOLD, 14);
    private static Color tooltipBorderColor = new Color(65, 11, 67);
    private int tooltipWidth;

    public boolean isBeingDragged, didStartDrag, mouseHover, setFontMetrics, isShowingTooltip;

    public int amount;
    public GameItem item;
    public InventorySlot slot;

    public ItemStack(GameItem item) {
        this(item, 1);
    }

    public ItemStack(GameItem item, int amount) {
        this.item = item;
        add(amount);
    }

    public void add(int number) {
        if (isFull()) {
            return;
        }
        amount += number;
        if (amount > MAX_AMOUNT) {
            amount = MAX_AMOUNT;
        }
    }

    public void take(int number) {
        amount -= number;
        if (amount < 0) {
            amount = 0;
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(item.image, x(), y(), null);
        graphics.setColor(Color.BLACK);
        graphics.drawString(String.valueOf(amount), x(), y() - 4);
        if (!setFontMetrics) {
            tooltipWidth = graphics.getFontMetrics(tooltipFont).stringWidth(item.displayName);
            setFontMetrics = true;
        }
        if (isShowingTooltip) {
            drawToolTip(graphics);
        }
    }

    public void tick() {
        mouseHover = Util.isWithinBounds(Mouse.x, Mouse.y, x(), y(), InventorySlot.SIZE, InventorySlot.SIZE);
        isShowingTooltip = isBeingDragged || (mouseHover && InventoryPanel.stackBeingDragged == null);
        if (Mouse.isDragging) {
            if ((mouseHover || didStartDrag) && (InventoryPanel.stackBeingDragged == this || InventoryPanel.stackBeingDragged == null)) {
                InventoryPanel.stackBeingDragged = this;
                setPosition(Mouse.x - (InventorySlot.SIZE / 2), Mouse.y - (InventorySlot.SIZE / 2));
                didStartDrag = true;
                isBeingDragged = true;
            }
        } else if (isBeingDragged) {
            onDragEnd();
            InventoryPanel.stackBeingDragged = null;
            isBeingDragged = false;
        } else {
            setPosition(slot.baseItemX, slot.baseItemY);
            didStartDrag = false;
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
        graphics.drawString(item.displayName, Mouse.x + 5, Mouse.y - 10);
    }

    private void onDragEnd() {
        ArrayList<Rectangle> intersectionRectangles = new ArrayList<>();
        ArrayList<InventorySlot> intersectedSlots = new ArrayList<>();

        for (InventorySlot slot : InventoryPanel.slots) {
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
                this.swap(closestSlot.stack);
            }
        } else {
            // did not end over any slots
            InventoryPanel i = Game.in().playerMenu.inventoryPanel;
            if (!Util.isWithinBounds(x(), y(), i.x(), i.y(), i.width, i.height)) {
                drop();
            }
        }
    }

    /**
     * Removes this item from the inventory
     */
    private void selfDestruct() {
        slot.removeStack();
    }

    private void move(InventorySlot moveTo) {
        slot.removeStack();
        slot = moveTo;
        moveTo.set(this);
    }

    private void swap(ItemStack swapWith) {
        InventorySlot slot1 = this.slot;
        ItemStack stack1 = this;
        InventorySlot slot2 = swapWith.slot;
        ItemStack stack2 = swapWith;
        slot1.set(stack2);
        slot2.set(stack1);
    }

    private void drop() {
        Game.in().map.sector.addEntity(new DroppedItem(item, Mouse.x - 8, Mouse.y - 8));
        selfDestruct();
    }

    public boolean isFull() {
        return amount < MAX_AMOUNT;
    }

}
