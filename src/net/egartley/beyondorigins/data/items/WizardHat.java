package net.egartley.beyondorigins.data.items;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.gamelib.abstracts.GameItem;

public class WizardHat extends GameItem {

    public WizardHat() {
        super("wizhat", "Wizard's Hat", ImageStore.get("resources/images/items/wizard-hat.png"));
    }
}
