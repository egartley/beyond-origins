package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.graphics.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

import java.awt.*;
import java.util.ArrayList;

public class Inventory extends StaticEntity {

    public int size = 36;
    public ArrayList<InventorySlot> slots;
    public ArrayList<GameItem> items;

    private Color backgroundColor = new Color(0, 0, 0, 152);

    public Inventory(Sprite sprite) {
        super("Inventory", sprite);
        slots = new ArrayList<>();
        items = new ArrayList<>();

        for (int r = 0; r < (int) Math.sqrt(size); r++) {
            for (int c = 0; c < (int) Math.sqrt(size); c++) {
                slots.add(new InventorySlot(r, c));
            }
        }

        items.add(new GameItem("Test Item", slots.get(3), null));
        slots.get(3).item = items.get(0);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);

        graphics.drawImage(sprite.toBufferedImage(), 289, 145, null);

        for (InventorySlot s : slots)
            s.render(graphics);
    }

    @Override
    public void tick() {
    }

    @Override
    protected void setBoundaries() {

    }

    @Override
    protected void setCollisions() {

    }
}
