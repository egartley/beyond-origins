package net.egartley.gamelib.abstracts;

import java.awt.image.BufferedImage;

public abstract class GameItem {

    public String id;
    public String displayName;
    public BufferedImage image;

    public GameItem(String id, String displayName, BufferedImage image) {
        this.id = id;
        this.displayName = displayName;
        this.image = image;
    }

}
