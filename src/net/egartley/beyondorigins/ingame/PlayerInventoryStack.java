package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.Renderable;
import net.egartley.beyondorigins.core.input.Mouse;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import net.egartley.beyondorigins.core.logic.inventory.ItemStack;
import net.egartley.beyondorigins.core.ui.PlayerInventory;
import net.egartley.beyondorigins.entities.DroppedItem;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.util.ArrayList;

public class PlayerInventoryStack extends Renderable implements Tickable {

    public static int MAX_AMOUNT = 99;

    private static final Font amountFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12), true);
    private int tooltipWidth;

    public boolean isBeingDragged, didStartDrag, mouseHover, setFontMetrics, isShowingTooltip;

    public ItemStack itemStack;
    public PlayerInventorySlot slot;

    public PlayerInventoryStack(ItemStack itemStack, PlayerInventorySlot slot) {
        this.itemStack = itemStack;
        this.slot = slot;
        setPosition(slot.baseItemX, slot.baseItemY);
    }

    public void tick() {
        mouseHover = Util.isWithinBounds(Mouse.x, Mouse.y, x(), y(), PlayerInventorySlot.SIZE, PlayerInventorySlot.SIZE);
        isShowingTooltip = isBeingDragged || (mouseHover && PlayerInventory.stackBeingDragged == null);
        if (isShowingTooltip) {
            PlayerInventory.tooltipWidth = tooltipWidth;
            PlayerInventory.tooltipText = itemStack.item.displayName;
        }
        if (Mouse.isDragging) {
            if ((mouseHover || didStartDrag) && (PlayerInventory.stackBeingDragged == this || PlayerInventory.stackBeingDragged == null)) {
                PlayerInventory.stackBeingDragged = this;
                setPosition(Mouse.x - (PlayerInventorySlot.SIZE / 2), Mouse.y - (PlayerInventorySlot.SIZE / 2));
                didStartDrag = true;
                isBeingDragged = true;
            }
        } else if (isBeingDragged) {
            onDragEnd();
            PlayerInventory.stackBeingDragged = null;
            isBeingDragged = false;
        } else {
            setPosition(slot.baseItemX, slot.baseItemY);
            didStartDrag = false;
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(itemStack.item.image, x(), y());
        graphics.setColor(Color.white);
        graphics.setFont(amountFont);
        if (itemStack.amount > 1) {
            if (itemStack.amount < 10) {
                graphics.drawString(String.valueOf(itemStack.amount), x() + PlayerInventorySlot.SIZE - 12, y() + PlayerInventorySlot.SIZE - 17);
            } else {
                graphics.drawString(String.valueOf(itemStack.amount), x() + PlayerInventorySlot.SIZE - 18, y() + PlayerInventorySlot.SIZE - 17);
            }
        }
        if (!setFontMetrics) {
            tooltipWidth = PlayerInventory.tooltipFont.getWidth(itemStack.item.displayName);
            setFontMetrics = true;
        }
    }

    private void onDragEnd() {
        ArrayList<Rectangle> intersectionRectangles = new ArrayList<>();
        ArrayList<PlayerInventorySlot> intersectedSlots = new ArrayList<>();

        for (PlayerInventorySlot slot : PlayerInventory.slots) {
            Rectangle r1 = new Rectangle(slot.x(), slot.y(), PlayerInventorySlot.SIZE, PlayerInventorySlot.SIZE);
            Rectangle r2 = new Rectangle(x(), y(), PlayerInventorySlot.SIZE, PlayerInventorySlot.SIZE);
            if (r1.intersects(r2)) {
                intersectionRectangles.add(r1.intersection(r2));
                intersectedSlots.add(slot);
                if (intersectionRectangles.size() == 4) {
                    // stack can only be within bounds of up to four slots
                    break;
                }
            }
        }

        if (!intersectionRectangles.isEmpty()) {
            // at least one slot
            int i = 0, n = 0;
            Rectangle closest = intersectionRectangles.get(0);
            // find the slot the stack is closet to
            for (Rectangle r : intersectionRectangles) {
                if ((r.width * r.height) > (closest.width * closest.height)) {
                    closest = r;
                    i = n;
                }
                n++;
            }
            // move the stack to the slot it is closest to
            PlayerInventorySlot closestSlot = intersectedSlots.get(i);
            if (closestSlot.isEmpty()) {
                // simply move the stack
                PlayerInventory.moveStackToEmpty(this, closestSlot.index);
            } else {
                // swap with the stack in the closest slot
                PlayerInventory.swapStacks(this, closestSlot.stack);
            }
        } else {
            // did not end over any slots
            PlayerInventory panel = InGameState.playerMenu.inventoryPanel;
            if (!Util.isWithinBounds(x(), y(), panel.x(), panel.y(), panel.width, panel.height)) {
                drop();
            }
        }
    }

    private void drop() {
        InGameState.map.sector.addEntity(new DroppedItem(itemStack, Mouse.x - 8, Mouse.y - 8));
        slot.clear();
    }

}
