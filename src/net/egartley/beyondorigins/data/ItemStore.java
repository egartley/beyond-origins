package net.egartley.beyondorigins.data;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.ingame.Item;

import java.util.ArrayList;

public class ItemStore {

    private static ArrayList<Item> items = new ArrayList<>();

    public static int amount = 0;

    public static void register(Item item) {
        if (!item.isRegistered) {
            items.add(item);
            item.isRegistered = true;
            amount++;
        } else {
            Debug.warning("Tried to add an item (" + item + ") to the store, but it's already in the store");
        }
    }

    public static void remove(Item item) {
        if (item.isRegistered) {
            items.remove(item);
            item.isRegistered = false;
            amount--;
        } else {
            Debug.warning("Tried to remove an item (" + item + ") from the store, but it was not in the store");
        }
    }

}
