package net.egartley.gamelib.graphics;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.data.Images;

import java.awt.image.BufferedImage;

public class MapTile {

    public static final String GRASS = "grass-default";
    public static final String GRASS_PATH_1 = "grass-path-1";
    public static final String GRASS_PATH_2 = "grass-path-2";
    public static final String SAND = "sand-default";

    public String id;
    public BufferedImage image;

    public MapTile(String id) {
        this(id, Images.get(Images.mapTilePath + id + ".png"));
    }

    public MapTile(String id, BufferedImage image) {
        this.id = id;
        this.image = image;
    }

    public void rotate() {
        image = Util.rotateImage(image);
    }

    public void rotate(double radians) {
        image = Util.rotateImage(image, radians);
    }

}
