package net.egartley.beyondorigins.core.logic.inventory;

import net.egartley.beyondorigins.core.abstracts.GameItem;

public class ItemStack {

    public int amount;
    public static final int MAX_AMOUNT = 99;
    public GameItem item;

    public ItemStack(GameItem item) {
        this(item, 1);
    }

    public ItemStack(GameItem item, int amount) {
        this.item = item;
        add(amount);
    }

    public void add(int number) {
        amount += number;
        if (amount > MAX_AMOUNT) {
            amount = MAX_AMOUNT;
        }
    }

    public void take(int number) {
        amount -= number;
        if (amount < 0) {
            amount = 0;
        }
    }

    public boolean isFull() {
        return amount == MAX_AMOUNT;
    }

    public String toString() {
        return item.displayName + " (" + amount + ")";
    }

}
