package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.objects.StaticEntity;

import java.awt.*;
import java.util.ArrayList;

public class Inventory extends StaticEntity {

    public static final int ROWS = 5, COLUMNS = 4;

    public static ArrayList<InventorySlot> slots;
    public static ArrayList<InventoryItem> items;

    private Color backgroundColor = new Color(0, 0, 0, 152);

    public Inventory(Sprite sprite) {
        super("Inventory", sprite);
        slots = new ArrayList<>();
        items = new ArrayList<>();
        setPosition((Game.WINDOW_WIDTH / 2) - (sprite.width / 2), (Game.WINDOW_HEIGHT / 2) - (sprite.height / 2));

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                slots.add(new InventorySlot(r, c));
            }
        }

        // temp
        items.add(new InventoryItem(Item.TEST_ITEM, slots.get(0), true));
        items.add(new InventoryItem(Item.TEST_ITEM, slots.get(1), true));
        items.add(new InventoryItem(Item.TEST_ITEM, slots.get(2), true));
    }

    static InventoryItem getItemBeingDragged() {
        for (InventoryItem i : items)
            if (i.isBeingDragged)
                return i;
        return null;
    }

    private int getSlotIndexFromRowColumn(int row, int column) {
        return row * ROWS + column;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);

        graphics.drawImage(sprite.toBufferedImage(), x(), y(), null);

        for (InventorySlot s : slots)
            s.render(graphics);

        for (InventoryItem i : items)
            i.render(graphics);
        for (InventoryItem i : items)
            i.drawToolTip(graphics);
    }

    @Override
    public void tick() {
        for (InventoryItem i : items)
            i.tick();
    }

    @Override
    protected void setBoundaries() {

    }

}
