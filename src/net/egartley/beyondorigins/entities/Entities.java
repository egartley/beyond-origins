package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.graphics.Sprite;
import net.egartley.beyondorigins.graphics.SpriteSheet;
import net.egartley.beyondorigins.ingame.DialoguePanel;
import net.egartley.beyondorigins.media.images.ImageStore;

public class Entities {

    public static final byte TEMPLATE_TREE = 1, TEMPLATE_ROCK = 2, TEMPLATE_DIALOGUE = 3, TEMPLATE_INVENTORY = 4;

    public static Player PLAYER;
    public static Dummy DUMMY;
    public static DialoguePanel DIALOGUE_PANEL;

    public static Sprite getTemplate(byte id) {
        switch (id) {
            case TEMPLATE_TREE:
                return new SpriteSheet(ImageStore.get(ImageStore.TREE_DEFAULT), 1, 1).getSprite(0);
            case TEMPLATE_ROCK:
                return new SpriteSheet(ImageStore.get(ImageStore.ROCK_DEFAULT), 1, 1).getSprite(0);
            case TEMPLATE_DIALOGUE:
                return new SpriteSheet(ImageStore.get(ImageStore.DIALOGUE_PANEL), 1, 1).getSprite(0);
            case TEMPLATE_INVENTORY:
                return new SpriteSheet(ImageStore.get(ImageStore.INVENTORY), 412, 252, 1, 1).getSprite(0);
            default:
                return null;
        }
    }

}
