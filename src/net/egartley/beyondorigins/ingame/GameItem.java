package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.graphics.Sprite;

import java.awt.*;

public class GameItem {

    public String name;
    public Sprite sprite;
    public InventorySlot slot;

    public GameItem(String name, InventorySlot slot, Sprite sprite) {
        this.name = name;
        this.slot = slot;
        this.sprite = sprite;
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillOval(slot.x + 2, slot.y + 2, InventorySlot.SIZE - 4, InventorySlot.SIZE - 4);
    }

}
