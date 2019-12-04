package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.ui.InventoryPanel;
import net.egartley.beyondorigins.ui.UIElement;
import net.egartley.gamelib.logic.inventory.ItemStack;

import java.awt.*;

public class InventorySlot extends UIElement {

    public static final int MARGIN = 3, SIZE = 36;

    public int index;
    public int baseItemX;
    public int baseItemY;
    public ItemStack stack;

    public InventorySlot(ItemStack stack, int row, int column) {
        super(ImageStore.get("resources/images/ui/inventory-slot.png"));
        set(stack);
        x((column * (SIZE + MARGIN)) + ((Game.WINDOW_WIDTH / 2) - (ImageStore.get(ImageStore.INVENTORY_PANEL).getWidth() / 2)) + 24);
        y((row * (SIZE + MARGIN)) + (((Game.WINDOW_HEIGHT / 2) + 12) - ((PlayerMenu.ROWS * (SIZE + MARGIN)) / 2)));
        baseItemX = x() + 2;
        baseItemY = y() + 2;
        index = row * InventoryPanel.ROWS + column;
    }

    public void set(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void tick() {
        if (!isEmpty() && stack != null) {
            stack.tick();
        }
    }

    public ItemStack removeStack() {
        ItemStack old = stack;
        stack = null;
        return old;
    }

    public void renderStack(Graphics graphics) {
        if (stack != null) {
            stack.render(graphics);
        }
    }

    public boolean isEmpty() {
        return stack == null;
    }

}
