package net.egartley.beyondorigins.core.logic.inventory;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.abstracts.GameItem;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityInventory {

    public static int DEFAULT_SLOTS = 3;

    private final ArrayList<ItemStack> slots;

    public Entity parent;

    public EntityInventory(Entity parent) {
        this(parent, DEFAULT_SLOTS);
    }

    public EntityInventory(Entity parent, int numberOfSlots) {
        this.parent = parent;
        slots = new ArrayList<>(numberOfSlots);
        for (int i = 0; i < numberOfSlots; i++) {
            slots.add(null);
        }
    }

    public void onUpdate() {

    }

    public ItemStack getStack(int index) {
        return slots.get(index);
    }

    public void set(ItemStack stack, int index) {
        slots.set(index, stack);
        onUpdate();
    }

    public int nextEmptySlot() {
        for (int i = 0; i < slots.size(); i++) {
            if (isEmpty(i)) {
                return i;
            }
        }
        // -1 to indicate that there are no empty slots (full)
        return -1;
    }

    public int firstAvailableSlotFor(GameItem item) {
        if (isEmpty()) {
            return 0;
        }
        // first check for filled slots with the same item
        for (int i = 0; i < slots.size(); i++) {
            if (isEmpty(i)) {
                continue;
            }
            ItemStack stack = slots.get(i);
            if (stack.item.is(item)) {
                if (stack.isFull()) {
                    continue;
                }
                return i;
            }
        }
        return nextEmptySlot();
    }

    public boolean isFull() {
        return nextEmptySlot() == -1;
    }

    public boolean isEmpty() {
        for (int i = 0; i < slots.size(); i++) {
            if (!isEmpty(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty(int index) {
        if (index >= 0 && index < slots.size()) {
            return slots.get(index) == null;
        } else {
            Debug.warning("Tried to get if slot " + index + " is empty in " + parent + "'s inventory, but it's out of bounds (size " + slots.size() + ")");
            return false;
        }
    }

    public boolean put(GameItem item) {
        return put(new ItemStack(item));
    }

    public boolean put(ItemStack stack) {
        return put(stack.item, stack.amount);
    }

    public boolean put(GameItem item, int amount) {
        int index = firstAvailableSlotFor(item);
        if (index == -1) {
            // there's no where to put the item(s)
            Debug.out("Couldn't find anywhere to put " + amount + " of " + item);
            return false;
        }
        if (isEmpty(index)) {
            return put(item, amount, index);
        } else {
            ItemStack mergeStack = getStack(index);
            int mergeAmount = mergeStack.amount;
            boolean overflow = mergeAmount + amount > ItemStack.MAX_AMOUNT;
            if (!overflow) {
                mergeStack.add(amount);
                set(mergeStack, index);
                return true;
            } else {
                int diff = ItemStack.MAX_AMOUNT - mergeAmount;
                mergeStack.add(diff);
                set(mergeStack, index);
                amount -= diff;
                return put(item, amount);
            }
        }
    }

    private boolean put(GameItem item, int amount, int slotIndex) {
        set(new ItemStack(item, amount), slotIndex);
        return true;
    }

    public boolean remove(GameItem item) {
        return remove(item, 1);
    }

    public boolean remove(GameItem item, int amount) {
        // first find the smallest stack
        int smallestIndex = -1, smallestAmount = ItemStack.MAX_AMOUNT + 1;
        for (int i = 0; i < slots.size(); i++) {
            if (!isEmpty(i)) {
                ItemStack stack = getStack(i);
                if (stack.item.is(item)) {
                    if (stack.amount < smallestAmount) {
                        smallestAmount = stack.amount;
                        smallestIndex = i;
                    }
                }
            }
        }
        if (smallestIndex == -1) {
            return false;
        }
        if (smallestAmount > amount) {
            // easy, just take away the amount and that's it
            ItemStack stack = getStack(smallestIndex);
            stack.take(amount);
            set(stack, smallestIndex);
        } else {
            // remove the stack, and call remove again with the remaining amount
            set(null, smallestIndex);
            return remove(item, amount - smallestAmount);
        }
        return false;
    }

    public boolean contains(GameItem item) {
        return contains(item, 1);
    }

    public boolean contains(GameItem item, int amount) {
        return contains(item, amount, false);
    }

    public boolean contains(GameItem item, int amount, boolean exact) {
        if (exact) {
            return amountOf(item) == amount;
        } else {
            return amountOf(item) >= amount;
        }
    }

    public int amountOf(GameItem item) {
        int amount = 0;
        for (int i = 0; i < slots.size(); i++) {
            if (isEmpty(i)) {
                continue;
            }
            ItemStack stack = slots.get(i);
            if (stack.item.is(item)) {
                amount += stack.amount;
            }
        }
        return amount;
    }

    @Override
    public String toString() {
        return Arrays.toString(slots.toArray());
    }

}
