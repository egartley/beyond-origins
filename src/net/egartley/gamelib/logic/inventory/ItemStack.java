package net.egartley.gamelib.logic.inventory;

import net.egartley.gamelib.abstracts.GameItem;

public class ItemStack {

    public static int MAX_AMOUNT = 99;

    public int amount;
    public GameItem item;

    public ItemStack(GameItem item) {
        this(item, 1);
    }

    public ItemStack(GameItem item, int amount) {
        this.item = item;
        add(amount);
    }

    public void add(int number) {
        if (isFull()) {
            return;
        }
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
        return amount < MAX_AMOUNT;
    }

}
