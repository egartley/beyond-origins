package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.abstracts.UIElement;
import net.egartley.beyondorigins.core.ui.PlayerInventory;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Graphics;

/**
 * A graphical slot in the player's inventory that the user can interact with and manipulate
 */
public class PlayerInventorySlot extends UIElement {

    private static int overallIndex = 0;

    public int index;
    public int baseItemX;
    public int baseItemY;
    public static final int SIZE = 36;
    public static final int MARGIN = 3;
    public PlayerInventoryStack stack;

    /**
     * Creates a new slot, and calculates its position based on the row and column
     *
     * @param stack The stack to display in the slot
     * @param row The slot's row
     * @param column The slot's column
     */
    public PlayerInventorySlot(PlayerInventoryStack stack, int row, int column) {
        super(Images.get("resources/images/ui/inventory-slot.png"));
        this.stack = stack;
        x((column * (SIZE + MARGIN)) + ((Game.WINDOW_WIDTH / 2) - (Images.get(Images.INVENTORY_PANEL).getWidth() / 2)) + 24);
        y((row * (SIZE + MARGIN)) + (((Game.WINDOW_HEIGHT / 2) + 12) - ((PlayerInventory.ROWS * (SIZE + MARGIN)) / 2)));
        baseItemX = x() + 2;
        baseItemY = y() + 2;
        index = overallIndex++;
    }

    @Override
    public void tick() {
        if (!isEmpty()) {
            stack.tick();
        }
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

}
