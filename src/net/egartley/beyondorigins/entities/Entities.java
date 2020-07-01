package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.data.Images;

/**
 * Commonly referenced entities, such as the player
 */
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
                return new SpriteSheet(Images.get(Images.TREE_DEFAULT), 1, 1).getSprite(0);
            case TEMPLATE_ROCK:
                return new SpriteSheet(Images.get(Images.ROCK_DEFAULT), 1, 1).getSprite(0);
            default:
                return new Sprite(Images.get("resources/images/unknown.png"));
        }
    }

}
