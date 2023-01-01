package net.egartley.beyondorigins.ingame.items;

import net.egartley.beyondorigins.engine.logic.inventory.GameItem;
import net.egartley.beyondorigins.data.Images;

public class CurrentYear extends GameItem {

    public CurrentYear() {
        super("currentyear", "IT'S CURRENT YEAR", Images.getImageFromPath("resources/images/items/current-year.png"));
    }

}
