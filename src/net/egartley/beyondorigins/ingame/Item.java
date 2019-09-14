package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.data.ImageStore;

import java.awt.image.BufferedImage;

public enum Item {

    WOJAK("wojak", "Feels bad, man"),
    HONKLER("honkler", "Honkler"), // clown world
    BOOMER("boomer", "30 year-old Boomer"),
    ZOOMER("zoomer", "Fortnite Zoomer"),
    BIG_CHUNGUS("big-chungus", "Big Chungus"),
    THE_ZUCC("the-zucc", "The ZUCC"),
    BITCONNECT("bitconnect", "BITCONNNNNNEEECCCTTT"),
    CURRENT_YEAR("current-year", "It's current year!"),
    AINT_CLICKIN_THAT("aint-clickin-that", "Ain't clickin' that"),
    HMM("hmm", "Thinking face"),
    TUCKER("tucker", "Tucker Carlson"),
    WIZARD_HAT("wizard-hat", "Wizard Hat");

    public String id;
    public String name;
    public BufferedImage image;

    Item(String id, String name) {
        this(id, name, id + ".png");
    }

    Item(String id, String name, String imageFileName) {
        this(id, name, ImageStore.get("resources/images/items/" + imageFileName));
    }

    Item(String id, String name, BufferedImage image) {
        this.id = id + "-item";
        this.name = name;
        this.image = image;
    }

}
