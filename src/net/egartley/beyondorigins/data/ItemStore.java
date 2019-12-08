package net.egartley.beyondorigins.data;

import net.egartley.beyondorigins.data.items.WizardHat;
import net.egartley.gamelib.abstracts.GameItem;

public class ItemStore {

    public static GameItem WIZARD_HAT;

    public static void init() {
        WIZARD_HAT = new WizardHat();
    }

}
