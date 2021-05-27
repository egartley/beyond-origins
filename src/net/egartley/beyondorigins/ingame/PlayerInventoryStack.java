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

    private int tooltipWidth;
    private static final Font AMOUNT_FONT = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12), true);

    public boolean isBeingHovered;
    public boolean didStartDrag;
    public boolean setTooltipWidth;
    public boolean isBeingDragged;
    public boolean isShowingTooltip;
    public static int MAX_AMOUNT = 99;
    public ItemStack itemStack;
    public PlayerInventorySlot slot;

    public PlayerInventoryStack(ItemStack itemStack, PlayerInventorySlot slot) {
        this.itemStack = itemStack;
        this.slot = slot;
        setPosition(slot.baseItemX, slot.baseItemY);
    }

    private void onDragEnd() {
        ArrayList<Rectangle> intersectionRectangles = new ArrayList<>();
        ArrayList<PlayerInventorySlot> intersectedSlots = new ArrayList<>();
        for (PlayerInventorySlot slot : PlayerInventory.slots) {
            Rectangle r1 = new Rectangle(slot.x, slot.y, PlayerInventorySlot.SIZE, PlayerInventorySlot.SIZE);
            Rectangle r2 = new Rectangle(x, y, PlayerInventorySlot.SIZE, PlayerInventorySlot.SIZE);
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
            if (!Util.isWithinBounds(x, y, panel.x, panel.y, panel.width, panel.height)) {
                drop();
            }
        }
    }

    private void drop() {
        InGameState.map.sector.addEntity(new DroppedItem(itemStack, Mouse.x - 8, Mouse.y - 8));
        slot.clear();
    }

    @Override
    public void tick() {
        // check if the cursor is over this stack
        isBeingHovered = Util.isWithinBounds(Mouse.x, Mouse.y, x, y, PlayerInventorySlot.SIZE, PlayerInventorySlot.SIZE);
        // whether or not to render the tooltip
        isShowingTooltip = isBeingDragged || (isBeingHovered && PlayerInventory.stackBeingDragged == null);
        if (isShowingTooltip) {
            PlayerInventory.tooltipWidth = tooltipWidth;
            PlayerInventory.tooltipText = itemStack.item.displayName;
        }
        if (Mouse.isDragging) {
            // user is dragging, anchor to cursor position
            if ((isBeingHovered || didStartDrag) && (PlayerInventory.stackBeingDragged == this || PlayerInventory.stackBeingDragged == null)) {
                PlayerInventory.stackBeingDragged = this;
                // keep cursor centered
                setPosition(Mouse.x - (PlayerInventorySlot.SIZE / 2), Mouse.y - (PlayerInventorySlot.SIZE / 2));
                didStartDrag = true;
                isBeingDragged = true;
            }
        } else if (isBeingDragged) {
            // user stopped dragging, so now we need to stop as well
            onDragEnd();
            PlayerInventory.stackBeingDragged = null;
            isBeingDragged = false;
        } else {
            setPosition(slot.baseItemX, slot.baseItemY);
            didStartDrag = false;
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(itemStack.item.image, x, y);
        graphics.setColor(Color.white);
        graphics.setFont(AMOUNT_FONT);
        if (itemStack.quantity > 1) {
            int offset = itemStack.quantity < 10 ? 12 : 18;
            graphics.drawString(String.valueOf(itemStack.quantity), x + PlayerInventorySlot.SIZE - offset, y + PlayerInventorySlot.SIZE - 17);
        }
        if (!setTooltipWidth) {
            tooltipWidth = PlayerInventory.TOOLTIP_FONT.getWidth(itemStack.item.displayName);
            setTooltipWidth = true;
        }
    }

}
