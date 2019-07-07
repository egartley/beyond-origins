package net.egartley.gamelib.objects;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.media.images.ImageStore;

import java.awt.image.BufferedImage;

/**
 * A "tile" that is rendered within a map sector
 */
public class MapTile {

    public static final byte GRASS = 0, SAND = 1, GRASS_PATH_1 = 2, GRASS_PATH_2 = 3;

    private static MapTile grass, sand, grassPath1, grassPath2;

    /**
     * This tile as a {@link BufferedImage}
     */
    BufferedImage bufferedImage;
    public int width, height;
    public boolean isTraversable;

    /**
     * Creates a new map tile
     */
    public MapTile(BufferedImage image) {
        bufferedImage = image;
        width = image.getWidth();
        height = image.getHeight();
    }

    public void rotate() {
        bufferedImage = Util.rotateImage(bufferedImage);
    }

    public void rotate(double radians) {
        bufferedImage = Util.rotateImage(bufferedImage, radians);
    }

    public static void init() {
        grass = new MapTile(ImageStore.get(ImageStore.TILE_GRASS));
        sand = new MapTile(ImageStore.get(ImageStore.TILE_SAND));
        grassPath1 = new MapTile(ImageStore.get("resources/images/map-tiles/grass-path-1.png"));
        grassPath2 = new MapTile(ImageStore.get("resources/images/map-tiles/grass-path-2.png"));
    }

    public static MapTile get(byte tileID) {
        switch (tileID) {
            case GRASS:
                return new MapTile(grass.bufferedImage);
            case SAND:
                return new MapTile(sand.bufferedImage);
            case GRASS_PATH_1:
                return new MapTile(grassPath1.bufferedImage);
            case GRASS_PATH_2:
                return new MapTile(grassPath2.bufferedImage);
            default:
                return null;
        }
    }

}
