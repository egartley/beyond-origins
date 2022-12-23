package net.egartley.beyondorigins.data;

import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.io.File;

public class Images {

    public static String path = "resources/images/";
    public static String uiPath = path + "ui/";
    public static String entityPath = path + "entities/";
    public static String mapTilePath = path + "map-tiles/";
    public static String expressionPath = entityPath + "expressions/";
    public static String bossPath = entityPath + "boss/";
    public final static byte PLAYER = 0;
    public final static byte DUMMY = 1;
    public final static byte TILE_GRASS = 2;
    public final static byte TILE_SAND = 3;
    public final static byte TREE_DEFAULT = 4;
    public final static byte ROCK_DEFAULT = 5;
    public final static byte FH = 6;
    public final static byte DIALOGUE_PANEL = 7;
    public final static byte INVENTORY_PANEL = 8;
    public final static byte MORE_LINES = 9;
    public final static byte INVENTORY_SLOT = 10;
    public final static byte WIZARD_DEFAULT = 11;
    public final static byte WIZARD_WITH_HAT = 12;
    public final static byte WIZARD_HAT = 13;
    public final static byte QUESTS_PANEL = 14;
    public final static byte QUEST_SLOT = 15;
    public final static byte QUEST_SLOT_HOVER = 16;
    public final static byte QUEST_CHECKBOX = 17;
    public final static byte QUEST_CHECKBOX_CHECKED = 18;
    public final static byte PAGE_BUTTON_ENABLED = 19;
    public final static byte PAGE_BUTTON_DISABLED = 20;
    public final static byte PAGE_BUTTON_HOVER = 21;
    public final static byte WARP_PAD = 22;
    public final static byte MONSTER = 23;
    public final static byte CUTSCENE = 24;
    public final static byte WIND_CHIMES = 25;
    public final static byte FH_WALK = 26;
    public final static byte FH_ATTACK1 = 27;
    public final static byte UNKNOWN = 100;

    /**
     * Returns the specified image
     */
    public static Image getImage(byte image) {
        return switch (image) {
            case PLAYER -> getImageFromPath(entityPath + "player-default.png");
            case DUMMY -> getImageFromPath(entityPath + "dummy.png");
            case WIZARD_DEFAULT -> getImageFromPath(entityPath + "wizard-default.png");
            case WIZARD_WITH_HAT -> getImageFromPath(entityPath + "wizard-with-hat.png");
            case MONSTER -> getImageFromPath(entityPath + "monster.png");
            case FH -> getImageFromPath(entityPath + "fh-default.png");
            case FH_WALK -> getImageFromPath(bossPath + "fh/fh-walk.png");
            case FH_ATTACK1 -> getImageFromPath(bossPath + "fh/fh-attack1.png");
            case WIND_CHIMES -> getImageFromPath(entityPath + "wind-chimes.png");
            case TREE_DEFAULT -> getImageFromPath(entityPath + "tree-default.png");
            case ROCK_DEFAULT -> getImageFromPath(entityPath + "rock-default.png");
            case WIZARD_HAT -> getImageFromPath(entityPath + "wizard-hat.png");
            case WARP_PAD -> getImageFromPath(entityPath + "warp-pad.png");
            case CUTSCENE -> getImageFromPath(entityPath + "cutscene-trigger.png");
            case DIALOGUE_PANEL -> getImageFromPath(uiPath + "dialogue-panel.png");
            case INVENTORY_PANEL -> getImageFromPath(uiPath + "inventory-panel.png");
            case QUESTS_PANEL -> getImageFromPath(uiPath + "quests-panel.png");
            case MORE_LINES -> getImageFromPath(uiPath + "more-lines.png");
            case INVENTORY_SLOT -> getImageFromPath(uiPath + "inventory-slot.png");
            case QUEST_SLOT -> getImageFromPath(uiPath + "quest-slot.png");
            case QUEST_SLOT_HOVER -> getImageFromPath(uiPath + "quest-slot_hover.png");
            case QUEST_CHECKBOX -> getImageFromPath(uiPath + "quest-checkbox.png");
            case QUEST_CHECKBOX_CHECKED -> getImageFromPath(uiPath + "quest-checkbox_checked.png");
            case PAGE_BUTTON_ENABLED -> getImageFromPath(uiPath + "page-button.png");
            case PAGE_BUTTON_DISABLED -> getImageFromPath(uiPath + "page-button_disabled.png");
            case PAGE_BUTTON_HOVER -> getImageFromPath(uiPath + "page-button_hover.png");
            default -> getImageFromPath(path + "unknown.png");
        };
    }

    /**
     * Returns an image at the specified path
     */
    public static Image getImageFromPath(String path) {
        // Credit: https://stackoverflow.com/a/23613661
        try {
            Texture texture = BufferedImageUtil.getTexture("", ImageIO.read(new File(path)));
            Image image = new Image(texture.getImageWidth(), texture.getImageHeight());
            image.setTexture(texture);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
