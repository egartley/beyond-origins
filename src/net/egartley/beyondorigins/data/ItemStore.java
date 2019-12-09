package net.egartley.beyondorigins.data;

import net.egartley.beyondorigins.data.items.CurrentYear;
import net.egartley.beyondorigins.data.items.Hmm;
import net.egartley.beyondorigins.data.items.WizardHat;
import net.egartley.gamelib.abstracts.GameItem;

public class ItemStore {

    public static GameItem WIZARD_HAT, HMM, CURRENT_YEAR;

    public static void init() {
        WIZARD_HAT = new WizardHat();
        HMM = new Hmm();
        CURRENT_YEAR = new CurrentYear();
    }

}
