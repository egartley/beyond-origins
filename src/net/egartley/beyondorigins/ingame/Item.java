package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.data.ItemStore;

import java.awt.image.BufferedImage;

public enum Item {

    TEST_ITEM("test_item", "wojak.png");

    public boolean isRegistered;

    public String name;
    public BufferedImage image;

    Item(String name, String imageFileName) {
        this(name, ImageStore.get("resources/images/items/" + imageFileName));
    }

    Item(String name, BufferedImage image) {
        this.name = name;
        this.image = image;

        ItemStore.register(this);
    }

}
