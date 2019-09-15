package net.egartley.beyondorigins.data;

import net.egartley.beyondorigins.Debug;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageStore {

    public final static byte PLAYER = 0;
    public final static byte DUMMY = 1;
    public final static byte WIZARD_DEFAULT = 11;
    public final static byte WIZARD_WITH_HAT = 12;

    public final static byte TILE_GRASS = 2;
    public final static byte TILE_SAND = 3;

    public final static byte TREE_DEFAULT = 4;
    public final static byte ROCK_DEFAULT = 5;
    public final static byte WIZARD_HAT = 13;

    public final static byte DIALOGUE_PANEL = 7;
    public final static byte INVENTORY_PANEL = 8;
    public final static byte MORE_LINES = 9;
    public final static byte INVENTORY_SLOT = 10;

    public static String path = "resources/images/";
    public static String entityPath = path + "entities/";
    public static String expressionPath = entityPath + "expressions/";
    public static String mapTilePath = path + "map-tiles/";
    public static String uiPath = path + "ui/";

    /**
     * Returns the specified image
     */
    public static BufferedImage get(byte image) {
        switch (image) {
            case PLAYER:
                return get(entityPath + "player-default.png");
            case DUMMY:
                return get(entityPath + "dummy.png");
            case WIZARD_DEFAULT:
                return get(entityPath + "wizard-default.png");
            case WIZARD_WITH_HAT:
                return get(entityPath + "wizard-with-hat.png");
            case TREE_DEFAULT:
                return get(entityPath + "tree-default.png");
            case ROCK_DEFAULT:
                return get(entityPath + "rock-default.png");
            case WIZARD_HAT:
                return get(entityPath + "wizard-hat.png");
            case DIALOGUE_PANEL:
                return get(uiPath + "dialogue-panel.png");
            case INVENTORY_PANEL:
                return get(uiPath + "inventory-panel.png");
            case MORE_LINES:
                return get(uiPath + "more-lines.png");
            case INVENTORY_SLOT:
                return get(uiPath + "inventory-slot.png");
            default:
                return null;
        }
    }

    /**
     * Returns an image at the specified path
     */
    public static BufferedImage get(String path) {
        BufferedImage r = null;
        try {
            r = ImageIO.read(new File(path));
        } catch (IOException e) {
            Debug.warning("There was an error while trying to read or find the image \"" + path + "\", using the \"unknown image\" file instead");
            try {
                r = ImageIO.read(new File("resources/images/unknown.png"));
            } catch (IOException e2) {
                Debug.error("Could not read or find the \"unknown image\" file (" + e2.getMessage() + ")");
            }
        }
        return r;
    }

}
