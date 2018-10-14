package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.SpriteSheet;

public class Entities {

    public static final byte TREE = 1, ROCK = 2;

    public static Player PLAYER;
    public static Dummy DUMMY;

    public static Sprite getSpriteTemplate(byte id) {
        switch (id) {
            case TREE:
                return new SpriteSheet(ImageStore.treeDefault, 1, 1).getSprite(0);
            case ROCK:
                return new SpriteSheet(ImageStore.rockDefault, 1, 1).getSprite(0);
            default:
                return null;
        }
    }

}
