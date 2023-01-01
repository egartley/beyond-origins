package net.egartley.beyondorigins.ingame.items;

import net.egartley.beyondorigins.engine.logic.inventory.GameItem;
import net.egartley.beyondorigins.data.Images;

public class WizardHat extends GameItem {

    public WizardHat() {
        super("wizhat", "Wizard's Hat", Images.getImageFromPath("images/items/wizard-hat.png"));
    }
}
