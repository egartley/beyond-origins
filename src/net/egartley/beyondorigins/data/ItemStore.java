package net.egartley.beyondorigins.data;

import net.egartley.beyondorigins.data.items.WizardHat;
import net.egartley.gamelib.abstracts.GameItem;

public class ItemStore {

    public static final byte WIZARD_HAT = 1;

    public static GameItem get(byte id) {
        switch (id) {
            case WIZARD_HAT:
                return new WizardHat();
        }
        return null;
    }

}
