package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.ui.UIElement;

import java.awt.*;

public class PlayerInventorySlot extends UIElement {

    private static int indexi = 0;

    public static final int MARGIN = 3, SIZE = 36;

    public int index;
    public int baseItemX;
    public int baseItemY;
    public PlayerInventoryStack stack;

    public PlayerInventorySlot(PlayerInventoryStack stack, int row, int column) {
        super(ImageStore.get("resources/images/ui/inventory-slot.png"));
        this.stack = stack;
        x((column * (SIZE + MARGIN)) + ((Game.WINDOW_WIDTH / 2) - (ImageStore.get(ImageStore.INVENTORY_PANEL).getWidth() / 2)) + 24);
        y((row * (SIZE + MARGIN)) + (((Game.WINDOW_HEIGHT / 2) + 12) - ((PlayerMenu.ROWS * (SIZE + MARGIN)) / 2)));
        baseItemX = x() + 2;
        baseItemY = y() + 2;
        index = indexi++;
    }

    @Override
    public void tick() {
        if (!isEmpty()) {
            stack.tick();
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x(), y(), null);
        graphics.setColor(Color.BLACK);
        graphics.drawString(String.valueOf(index), x() + SIZE - 8, y() + SIZE - 6);
    }

    public void renderStack(Graphics graphics) {
        if (!isEmpty()) {
            stack.render(graphics);
        }
    }

    public void clear() {
        stack = null;
    }

    public boolean isEmpty() {
        return stack == null;
    }

}
