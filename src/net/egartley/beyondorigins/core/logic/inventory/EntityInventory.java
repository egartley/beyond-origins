package net.egartley.beyondorigins.core.logic.inventory;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.abstracts.GameItem;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityInventory {

    private final ArrayList<ItemStack> slots;

    public static int DEFAULT_SLOTS = 3;
    public Entity entity;

    public EntityInventory(Entity entity) {
        this(entity, DEFAULT_SLOTS);
    }

    public EntityInventory(Entity entity, int numberOfSlots) {
        this.entity = entity;
        slots = new ArrayList<>(numberOfSlots);
        for (int i = 0; i < numberOfSlots; i++) {
            slots.add(null);
        }
    }

    public void onUpdate() {

    }

    public void setStackAt(ItemStack stack, int index) {
        slots.set(index, stack);
        onUpdate();
    }

    public int getAmount(GameItem item) {
        int amount = 0;
        for (int i = 0; i < slots.size(); i++) {
            if (isSlotEmpty(i)) {
                continue;
            }
            ItemStack stack = slots.get(i);
            if (stack.item.is(item)) {
                amount += stack.quantity;
            }
        }
        return amount;
    }

    public int getNextEmptySlotIndex() {
        for (int i = 0; i < slots.size(); i++) {
            if (isSlotEmpty(i)) {
                return i;
            }
        }
        // -1 to indicate that there are no empty slots (full)
        return -1;
    }

    public int getNextAvailableSlotIndex(GameItem item) {
        if (isEmpty()) {
            return 0;
        }
        // first check for filled slots with the same item
        for (int i = 0; i < slots.size(); i++) {
            if (isSlotEmpty(i)) {
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
        return getNextEmptySlotIndex();
    }

    public boolean isFull() {
        return getNextEmptySlotIndex() == -1;
    }

    public boolean isEmpty() {
        for (int i = 0; i < slots.size(); i++) {
            if (!isSlotEmpty(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isSlotEmpty(int index) {
        if (index >= 0 && index < slots.size()) {
            return slots.get(index) == null;
        } else {
            Debug.warning("Tried to get if slot " + index + " is empty in " + entity + "'s inventory, but it's out of bounds (size " + slots.size() + ")");
            return false;
        }
    }

    public boolean putItem(GameItem item, int amount) {
        int index = getNextAvailableSlotIndex(item);
        if (index == -1) {
            // there's no where to put the item(s)
            Debug.out("Couldn't find anywhere to put " + amount + " of " + item);
            return false;
        }
        if (isSlotEmpty(index)) {
            return putItem(item, amount, index);
        } else {
            ItemStack mergeStack = getStack(index);
            int mergeAmount = mergeStack.quantity;
            boolean overflow = mergeAmount + amount > ItemStack.MAX_AMOUNT;
            if (!overflow) {
                mergeStack.add(amount);
                setStackAt(mergeStack, index);
                return true;
            } else {
                int diff = ItemStack.MAX_AMOUNT - mergeAmount;
                mergeStack.add(diff);
                setStackAt(mergeStack, index);
                amount -= diff;
                return putItem(item, amount);
            }
        }
    }

    public boolean putItem(GameItem item) {
        return putStack(new ItemStack(item));
    }

    private boolean putItem(GameItem item, int amount, int slotIndex) {
        setStackAt(new ItemStack(item, amount), slotIndex);
        return true;
    }

    public boolean putStack(ItemStack stack) {
        return putItem(stack.item, stack.quantity);
    }

    public boolean removeItem(GameItem item) {
        return removeItem(item, 1);
    }

    public boolean removeItem(GameItem item, int amount) {
        // first find the smallest stack
        int smallestIndex = -1, smallestAmount = ItemStack.MAX_AMOUNT + 1;
        for (int i = 0; i < slots.size(); i++) {
            if (!isSlotEmpty(i)) {
                ItemStack stack = getStack(i);
                if (stack.item.is(item)) {
                    if (stack.quantity < smallestAmount) {
                        smallestAmount = stack.quantity;
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
            setStackAt(stack, smallestIndex);
        } else {
            // remove the stack, and call remove again with the remaining amount
            setStackAt(null, smallestIndex);
            return removeItem(item, amount - smallestAmount);
        }
        return false;
    }

    public boolean containsItem(GameItem item) {
        return containsItems(item, 1);
    }

    public boolean containsItems(GameItem item, int amount) {
        return getAmount(item) >= amount;
    }

    public boolean containsItemsExact(GameItem item, int amount) {
        return getAmount(item) == amount;
    }

    public ItemStack getStack(int index) {
        return slots.get(index);
    }

    @Override
    public String toString() {
        return Arrays.toString(slots.toArray());
    }

}
