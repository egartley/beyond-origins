package net.egartley.beyondorigins.data;

import net.egartley.beyondorigins.Debug;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageStore {

    public final static byte PLAYER = 0;
    public final static byte DUMMY = 1;

    public final static byte TILE_GRASS = 2;
    public final static byte TILE_SAND = 3;

    public final static byte TREE_DEFAULT = 4;
    public final static byte ROCK_DEFAULT = 5;

    public final static byte EXPRESSION_CONFUSION = 6;
    public final static byte EXPRESSION_CONCERN = 99;
    public final static byte EXPRESSION_ANGER = 98;
    public final static byte EXPRESSION_HEART = 97;

    public final static byte DIALOGUE_PANEL = 7;
    public final static byte INVENTORY = 8;
    public final static byte MORE_LINES = 9;

    /**
     * Returns the specified image
     */
    public static BufferedImage get(byte image) {
        String path = "resources/images/";
        String entityPath = path + "entities/";
        String expressionPath = entityPath + "expressions/";
        String mapTilePath = path + "map-tiles/";
        String uiPath = path + "ui/";
        switch (image) {
            case PLAYER:
                return get(entityPath + "player-default.png");
            case DUMMY:
                return get(entityPath + "dummy.png");
            case TILE_GRASS:
                return get(mapTilePath + "grass-default.png");
            case TILE_SAND:
                return get(mapTilePath + "sand-default.png");
            case TREE_DEFAULT:
                return get(entityPath + "tree-default.png");
            case ROCK_DEFAULT:
                return get(entityPath + "rock-default.png");
            case EXPRESSION_CONFUSION:
                return get(expressionPath + "confusion.png");
            case EXPRESSION_CONCERN:
                return get(expressionPath + "concern.png");
            case EXPRESSION_ANGER:
                return get(expressionPath + "anger.png");
            case EXPRESSION_HEART:
                return get(expressionPath + "heart.png");
            case DIALOGUE_PANEL:
                return get(uiPath + "dialogue-panel.png");
            case INVENTORY:
                return get(entityPath + "inventory.png");
            case MORE_LINES:
                return get(uiPath + "more-lines.png");
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
