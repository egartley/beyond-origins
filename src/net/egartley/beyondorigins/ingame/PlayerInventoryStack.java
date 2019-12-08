package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.entities.DroppedItem;
import net.egartley.beyondorigins.ui.InventoryPanel;
import net.egartley.gamelib.abstracts.Renderable;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.interfaces.Tickable;
import net.egartley.gamelib.logic.inventory.ItemStack;

import java.awt.*;
import java.util.ArrayList;

public class PlayerInventoryStack extends Renderable implements Tickable {

    public static int MAX_AMOUNT = 99;

    private static Font tooltipFont = new Font("Arial", Font.BOLD, 14);
    private static Color tooltipBorderColor = new Color(65, 11, 67);
    private int tooltipWidth;

    public boolean isBeingDragged, didStartDrag, mouseHover, setFontMetrics, isShowingTooltip;

    public ItemStack itemStack;
    public int bx, by;

    public PlayerInventoryStack(ItemStack itemStack, int bx, int by) {
        this.itemStack = itemStack;
        this.bx = bx;
        this.by = by;
        setPosition(bx, by);
    }

    public void tick() {
        Debug.out("ticking");
        mouseHover = Util.isWithinBounds(Mouse.x, Mouse.y, x(), y(), PlayerInventorySlot.SIZE, PlayerInventorySlot.SIZE);
        isShowingTooltip = isBeingDragged || (mouseHover && InventoryPanel.stackBeingDragged == null);
        if (Mouse.isDragging) {
            if ((mouseHover || didStartDrag) && (InventoryPanel.stackBeingDragged == this || InventoryPanel.stackBeingDragged == null)) {
                InventoryPanel.stackBeingDragged = this;
                setPosition(Mouse.x - (PlayerInventorySlot.SIZE / 2), Mouse.y - (PlayerInventorySlot.SIZE / 2));
                didStartDrag = true;
                isBeingDragged = true;
            }
        } else if (isBeingDragged) {
            onDragEnd();
            InventoryPanel.stackBeingDragged = null;
            isBeingDragged = false;
        } else {
            setPosition(bx, by);
            didStartDrag = false;
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(itemStack.item.image, x(), y(), null);
        graphics.setColor(Color.BLACK);
        graphics.drawString(String.valueOf(itemStack.amount), x(), y() - 4);
        if (!setFontMetrics) {
            tooltipWidth = graphics.getFontMetrics(tooltipFont).stringWidth(itemStack.item.displayName);
            setFontMetrics = true;
        }
        if (isShowingTooltip) {
            drawToolTip(graphics);
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
        graphics.drawString(itemStack.item.displayName, Mouse.x + 5, Mouse.y - 10);
    }

    private void onDragEnd() {
        ArrayList<Rectangle> intersectionRectangles = new ArrayList<>();
        ArrayList<PlayerInventorySlot> intersectedSlots = new ArrayList<>();

        for (PlayerInventorySlot slot : InventoryPanel.slots) {
            Rectangle r1 = new Rectangle(slot.x(), slot.y(), PlayerInventorySlot.SIZE, PlayerInventorySlot.SIZE);
            Rectangle r2 = new Rectangle(x(), y(), PlayerInventorySlot.SIZE, PlayerInventorySlot.SIZE);
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
            // move the stack to the slot it is closest to
            PlayerInventorySlot closestSlot = intersectedSlots.get(i);
            if (closestSlot.isEmpty()) {
                // simply move the stack
                InventoryPanel.moveStack(this, closestSlot);
            } else {
                // swap with the stack in the closest slot
                InventoryPanel.swapStacks(this, closestSlot.stack);
            }
        } else {
            // did not end over any slots
            InventoryPanel i = Game.in().playerMenu.inventoryPanel;
            if (!Util.isWithinBounds(x(), y(), i.x(), i.y(), i.width, i.height)) {
                drop();
            }
        }
    }

    private void drop() {
        Game.in().map.sector.addEntity(new DroppedItem(itemStack.item, Mouse.x - 8, Mouse.y - 8));
        // selfDestruct();
    }

}
