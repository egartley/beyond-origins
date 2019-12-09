package net.egartley.beyondorigins.data.items;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.gamelib.abstracts.GameItem;

public class CurrentYear extends GameItem {

    public CurrentYear() {
        super("currentyear", "IT'S CURRENT YEAR", ImageStore.get("resources/images/items/current-year.png"));
    }

}
