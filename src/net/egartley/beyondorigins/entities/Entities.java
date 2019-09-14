package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.graphics.SpriteSheet;

public class Entities {

    public static final byte TEMPLATE_TREE = 1, TEMPLATE_ROCK = 2;

    public static Player PLAYER;
    public static Dummy DUMMY;
    public static Wizard WIZARD;

    public static void initialize() {
        PLAYER = new Player();
        DUMMY = new Dummy();
        WIZARD = new Wizard();
    }

    public static Sprite getSpriteTemplate(byte id) {
        switch (id) {
            case TEMPLATE_TREE:
                return new SpriteSheet(ImageStore.get(ImageStore.TREE_DEFAULT), 1, 1).getSprite(0);
            case TEMPLATE_ROCK:
                return new SpriteSheet(ImageStore.get(ImageStore.ROCK_DEFAULT), 1, 1).getSprite(0);
            default:
                return new Sprite(ImageStore.get("resources/images/unknown.png"));
        }
    }

}
