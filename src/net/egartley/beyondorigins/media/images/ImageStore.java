package net.egartley.beyondorigins.media.images;

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

    public final static byte DIALOGUE_PANEL = 7;
    public final static byte INVENTORY = 8;

    public static BufferedImage get(byte image) {
        String path = "resources/images/";
        String mapTilePath = path + "map-tiles/";
        String entityPath = path + "entities/";
        String expressionPath = entityPath + "expressions/";
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
            case DIALOGUE_PANEL:
                return get(entityPath + "dialogue-panel.png");
            case INVENTORY:
                return get(entityPath + "inventory.png");
            default:
                return null;
        }
    }

    public static BufferedImage get(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            Debug.error("There was an error while trying to load an image");
            Debug.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
