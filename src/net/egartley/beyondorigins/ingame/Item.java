package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Image;

public enum Item {

    CURRENT_YEAR("current-year", "It's current year!"),
    HMM("hmm", "Thinking face"),
    WIZARD_HAT("wizard-hat", "Wizard Hat");

    public String id;
    public String name;
    public Image image;

    Item(String id, String name) {
        this(id, name, id + ".png");
    }

    Item(String id, String name, String imageFileName) {
        this(id, name, Images.get("resources/images/items/" + imageFileName));
    }

    Item(String id, String name, Image image) {
        this.id = id + "-item";
        this.name = name;
        this.image = image;
    }

}
