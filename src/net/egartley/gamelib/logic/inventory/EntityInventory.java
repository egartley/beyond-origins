package net.egartley.gamelib.logic.inventory;

import net.egartley.beyondorigins.Debug;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.abstracts.GameItem;

import java.util.ArrayList;

public class EntityInventory {

    public static int DEFAULT_SLOTS = 20;

    private ArrayList<ItemStack> slots;

    public Entity parent;

    public EntityInventory(Entity parent) {
        this(parent, DEFAULT_SLOTS);
    }

    public EntityInventory(Entity parent, int numberOfSlots) {
        this.parent = parent;
        slots = new ArrayList<>(numberOfSlots);
        slots.add(new ItemStack(null));
    }

    public int nextEmptySlot() {
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i) == null) {
                Debug.info("Next empty slot is " + i);
                return i;
            }
        }
        // -1 to indicate that there are no empty slots (full)
        return -1;
    }

    public int firstAvailableSlot(GameItem item) {
        if (isEmpty()) {
            return 0;
        }
        for (int i = 0; i < slots.size(); i++) {
            if (isEmpty(i)) {
                continue;
            }
            ItemStack stack = slots.get(i);
            if (stack.item.id.equals(item.id)) {
                if (stack.isFull()) {
                    continue;
                }
                return i;
            }
        }
        // -1 to indicate that there are no available slots for the item
        return -1;
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
        return put(item, 1);
    }

    public boolean put(GameItem item, int amount) {

        // TODO: when amount is over 99 (multiple stacks)

        return put(item, amount, firstAvailableSlot(item));
    }

    public boolean put(GameItem item, int amount, int slotIndex) {

        // TODO: when amount is over 99 (multiple stacks)

        if (isEmpty()) {
            slots.set(0, new ItemStack(item, amount));
        } else {
            slots.set(slotIndex, new ItemStack(item, amount));
        }

        return true;
    }

    public boolean remove(GameItem item) {
        return remove(item, 1);
    }

    public boolean remove(GameItem item, int amount) {

        // TODO: when amount is over 99 (multiple stacks)

        return !isEmpty();
    }

    public boolean contains(GameItem item) {
        return contains(item, 1);
    }

    public boolean contains(GameItem item, int amount) {

        // TODO: when amount is over 99 (multiple stacks)

        if (isEmpty()) {
            return false;
        }
        for (int i = 0; i < slots.size(); i++) {
            if (isEmpty(i)) {
                continue;
            }
            ItemStack stack = slots.get(i);
            if (stack.item.id.equals(item.id) && stack.amount == amount) {
                return true;
            }
        }
        return false;
    }

}
