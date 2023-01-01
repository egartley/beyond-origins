package net.egartley.beyondorigins.data;

import net.egartley.beyondorigins.ingame.quests.WizardHatQuest;

public class Quests {

    public static WizardHatQuest WIZARD_HAT;

    public static void init() {
        WIZARD_HAT = new WizardHatQuest();
    }

}
