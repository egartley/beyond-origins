package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.graphics.Sprite;
import net.egartley.beyondorigins.input.Mouse;

import java.awt.*;

public class GameItem {

    public int renderX, renderY;
    public boolean isBeingDragged;
    public String name;
    public Sprite sprite;
    public InventorySlot slot;

    public GameItem(String name, InventorySlot slot, Sprite sprite) {
        this.name = name;
        this.slot = slot;
        this.sprite = sprite;
        renderX = slot.baseItemX;
        renderY = slot.baseItemY;
    }

    public void tick() {
        // TODO: change to item width and height
        if (Mouse.isDragging) {
            if (Util.isWithinBounds(Mouse.x, Mouse.y, renderX, renderY, InventorySlot.SIZE, InventorySlot.SIZE)) {
                renderX = Mouse.x - (InventorySlot.SIZE / 2);
                renderY = Mouse.y - (InventorySlot.SIZE / 2);
            }
            isBeingDragged = true;
        } else if (isBeingDragged) {
            Game.inGameState.inventory.onItemDragEnd(this);
            isBeingDragged = false;
        } else {
            renderX = slot.baseItemX;
            renderY = slot.baseItemY;
        }
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillOval(renderX, renderY, InventorySlot.SIZE, InventorySlot.SIZE);
    }

}
