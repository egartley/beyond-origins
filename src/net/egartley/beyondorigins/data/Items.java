package net.egartley.beyondorigins.data;

import net.egartley.beyondorigins.ingame.items.CurrentYear;
import net.egartley.beyondorigins.ingame.items.Hmm;
import net.egartley.beyondorigins.ingame.items.WizardHat;
import net.egartley.gamelib.abstracts.GameItem;

public class Items {

    public static GameItem WIZARD_HAT, HMM, CURRENT_YEAR;

    public static void init() {
        WIZARD_HAT = new WizardHat();
        HMM = new Hmm();
        CURRENT_YEAR = new CurrentYear();
    }

}
