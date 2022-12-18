package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.data.Images;

/**
 * Commonly referenced entities, such as the player
 */
public class Entities {

    public static final byte TEMPLATE_WP = 3;
    public static final byte TEMPLATE_CT = 4;
    public static final byte TEMPLATE_TREE = 1;
    public static final byte TEMPLATE_ROCK = 2;
    public static Dummy DUMMY;
    public static Player PLAYER;
    public static Wizard WIZARD;

    public static void initialize() {
        PLAYER = new Player();
        DUMMY = new Dummy();
        WIZARD = new Wizard();
    }

    public static Sprite getSpriteTemplate(byte id) {
        return switch (id) {
            case TEMPLATE_TREE -> new SpriteSheet(
                    Images.getImage(Images.TREE_DEFAULT), 1, 1).getSprite(0);
            case TEMPLATE_ROCK -> new SpriteSheet(
                    Images.getImage(Images.ROCK_DEFAULT), 1, 1).getSprite(0);
            case TEMPLATE_WP -> new SpriteSheet(Images.getImage(Images.WARP_PAD), 1, 1).getSprite(0);
            case TEMPLATE_CT -> new SpriteSheet(Images.getImage(Images.CUTSCENE), 1, 1).getSprite(0);
            default -> new Sprite(Images.getImageFromPath("resources/images/unknown.png"));
        };
    }

}
