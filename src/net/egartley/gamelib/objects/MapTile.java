package net.egartley.gamelib.objects;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.data.ImageStore;

import java.awt.image.BufferedImage;

/**
 * A "tile" that is rendered within a map sector
 */
public class MapTile {

    public static MapTile GRASS = new MapTile(ImageStore.get("resources/images/map-tiles/grass-default.png"));
    public static MapTile SAND = new MapTile(ImageStore.get("resources/images/map-tiles/sand-default.png"));
    public static MapTile GRASS_PATH_1 = new MapTile(ImageStore.get("resources/images/map-tiles/grass-path-1.png"));
    public static MapTile GRASS_PATH_2 = new MapTile(ImageStore.get("resources/images/map-tiles/grass-path-2.png"));


    /**
     * This tile as a {@link BufferedImage}
     */
    BufferedImage image;
    public int width, height;
    public boolean isTraversable;

    /**
     * Creates a new map tile
     */
    MapTile(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }

    public MapTile rotate() {
        return new MapTile(Util.rotateImage(image));
    }

    public MapTile rotate(double radians) {
        return new MapTile(Util.rotateImage(image, radians));
    }

}
