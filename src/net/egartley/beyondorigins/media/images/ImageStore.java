package net.egartley.beyondorigins.media.images;

import net.egartley.beyondorigins.Debug;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageStore {

    public static BufferedImage grassDefault;
    public static BufferedImage sandDefault;
    public static BufferedImage playerDefault;
    public static BufferedImage dummy;
    public static BufferedImage treeDefault;
    public static BufferedImage rockDefault;

    public static void loadAll() {
        // TODO: Better handling of image paths (rather than hard-coded variables)
        String path = "resources/images/";
        String mapTilePath = path + "map-tiles/";
        String entityPath = path + "entities/";
        try {
            playerDefault = ImageIO.read(new File(entityPath + "player-default.png"));
            dummy = ImageIO.read(new File(entityPath + "dummy.png"));
            treeDefault = ImageIO.read(new File(entityPath + "tree-default.png"));
            rockDefault = ImageIO.read(new File(entityPath + "rock-default.png"));

            // TODO: Once there are more than ten or so tiles, make something that only keeps the needed tiles in
            // memory. Right now since there are only a few, it does not really matter
            grassDefault = ImageIO.read(new File(mapTilePath + "grass-default.png"));
            sandDefault = ImageIO.read(new File(mapTilePath + "sand-default.png"));
        } catch (IOException e) {
            Debug.error("There was an error while attempting to load source images!");
            if (e instanceof FileNotFoundException)
                Debug.error("(at least one of them does not exist)");
            e.printStackTrace();
        }
    }

}
