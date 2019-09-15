package net.egartley.gamelib.graphics;

import net.egartley.beyondorigins.data.ImageStore;

import java.awt.image.BufferedImage;

public enum MapTile {

    GRASS("grass-default"),
    GRASS_PATH_1("grass-path-1"),
    GRASS_PATH_2("grass-path-2"),
    SAND("sand-default");

    public String id;
    public BufferedImage image;

    MapTile(String id) {
        this.id = id;
        image = ImageStore.get(ImageStore.mapTilePath + id + ".png");
    }

    public static MapTile get(String id) {
        for (MapTile tile : MapTile.values()) {
            if (tile.id.equalsIgnoreCase(id)) {
                return tile;
            }
        }
        // return grass tile if not found
        return GRASS;
    }

}
