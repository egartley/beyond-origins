package net.egartley.beyondorigins.core.graphics;

import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Image;

public class MapTile {

    public String id;
    public Image image;
    public static final String SAND = "sand-default";
    public static final String GRASS = "grass-default";
    public static final String GRASS_PATH_1 = "grass-path-1";
    public static final String GRASS_PATH_2 = "grass-path-2";

    public MapTile(String id) {
        this(id, Images.get(Images.mapTilePath + id + ".png"));
    }

    public MapTile(String id, Image image) {
        this.id = id;
        this.image = image;
    }

    public void rotate() {
        rotate(90);
    }

    public void rotate(float angle) {
        image = image.copy();
        image.rotate(angle);
    }

}
