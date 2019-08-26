package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.data.ItemStore;

import java.awt.image.BufferedImage;

public enum Item {

    WOJAK("wojak", "Feels Bad, Man", "wojak.png"),
    HONKLER("honkler", "Honkler", "honkler.png"),
    BOOMER("boomer", "30 year-old Boomer", "boomer.png"),
    ZOOMER("zoomer", "Fortnite Zoomer", "zoomer.png"),
    BIG_CHUNGUS("big-chungus", "Big Chungus", "big-chungus.png"),
    THE_ZUCC("the-zucc", "The ZUCC", "the-zucc.png"),
    BITCONNECT("bitconnect", "BITCONNNNNNEEECCCTTT", "bitconnect.png"),
    CURRENT_YEAR("current-year", "It's current year!", "current-year.png"),
    AINT_CLICKIN_THAT("aint-clickin-that", "Ain't clickin' that", "aint-clickin-that.png"),
    HMM("hmm", "HMMMMM", "hmm.png"),
    TUCKER("tucker", "Tucker Carlson", "tucker.png");

    public boolean isRegistered;

    public String id;
    public String name;
    public BufferedImage image;

    Item(String id, String name, String imageFileName) {
        this(id, name, ImageStore.get("resources/images/items/" + imageFileName));
    }

    Item(String id, String name, BufferedImage image) {
        this.id = id + "_item";
        this.name = name;
        this.image = image;
        ItemStore.register(this);
    }

}
