package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.media.images.ImageStore;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * A "tile" that is rendered within a map sector
 */
public class MapTile {

    public static final byte GRASS = 0, SAND = 1, GRASS_PATH_1 = 2, GRASS_PATH_2 = 3;

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

    public static MapTile get(byte tileID) {
        switch (tileID) {
            case GRASS:
                return new MapTile(ImageStore.get(ImageStore.TILE_GRASS));
            case SAND:
                return new MapTile(ImageStore.get(ImageStore.TILE_SAND));
            case GRASS_PATH_1:
                return new MapTile(ImageStore.get("resources/images/map-tiles/grass-path-1.png"));
            case GRASS_PATH_2:
                return new MapTile(ImageStore.get("resources/images/map-tiles/grass-path-2.png"));
            default:
                return null;
        }
    }

    /**
     * Rotates {@link #bufferedImage} by the specified angle (clockwise, in radians)
     */
    public void rotate(double radians) {
        AffineTransform t = new AffineTransform();
        BufferedImage rotated = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        t.rotate(radians, bufferedImage.getWidth() / 2.0, bufferedImage.getHeight() / 2.0);
        rotated = new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(bufferedImage, rotated);
        bufferedImage = rotated;
    }

    /**
     * Rotates {@link #bufferedImage} by 90 degrees clockwise
     */
    public void rotate() {
        // rotate by 90 degrees if no angle is specified
        rotate(Math.PI / 2);
    }

}
