package net.egartley.beyondorigins.core.logic.inventory;

import net.egartley.beyondorigins.core.abstracts.GameItem;

public class ItemStack {

    public int quantity;
    public static final int MAX_AMOUNT = 99;
    public GameItem item;

    public ItemStack(GameItem item) {
        this(item, 1);
    }

    public ItemStack(GameItem item, int quantity) {
        this.item = item;
        add(quantity);
    }

    public void add(int number) {
        quantity += number;
        if (quantity > MAX_AMOUNT) {
            quantity = MAX_AMOUNT;
        }
    }

    public void take(int number) {
        quantity -= number;
        if (quantity < 0) {
            quantity = 0;
        }
    }

    public boolean isFull() {
        return quantity == MAX_AMOUNT;
    }

    public String toString() {
        return item.displayName + " (" + quantity + ")";
    }

}
