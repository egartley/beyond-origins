package net.egartley.beyondorigins.engine;

import org.newdawn.slick.Image;

public class LevelMapTile {

    public Image image;
    private String id;

    public LevelMapTile(String id, Image image) {
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

    public String getID() {
        return id;
    }

}
