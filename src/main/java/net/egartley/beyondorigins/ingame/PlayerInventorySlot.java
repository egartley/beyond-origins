package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.engine.ui.UIElement;
import net.egartley.beyondorigins.engine.ui.PlayerInventory;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Graphics;

public class PlayerInventorySlot extends UIElement {

    private static int overallIndex = 0;

    public int index;
    public int baseItemX;
    public int baseItemY;
    public static final int SIZE = 36;
    public static final int MARGIN = 3;
    public PlayerInventoryStack stack;

    public PlayerInventorySlot(PlayerInventoryStack stack, int row, int column) {
        super(Images.getImageFromPath("images/ui/inventory-slot.png"));
        this.stack = stack;
        x = (column * (SIZE + MARGIN)) + ((Game.WINDOW_WIDTH / 2) -
                (Images.getImage(Images.INVENTORY_PANEL).getWidth() / 2)) + 24;
        y = (row * (SIZE + MARGIN)) + (((Game.WINDOW_HEIGHT / 2) + 12) -
                ((PlayerInventory.ROWS * (SIZE + MARGIN)) / 2));
        baseItemX = x + 2;
        baseItemY = y + 2;
        index = overallIndex++;
    }

    public void renderStack(Graphics graphics) {
        if (!isEmpty()) {
            stack.render(graphics);
        }
    }

    public void clear() {
        stack = null;
        PlayerInventory.clearSlot(index);
    }

    public boolean isEmpty() {
        return stack == null;
    }

    @Override
    public void tick() {
        if (!isEmpty()) {
            stack.tick();
        }
    }

}
