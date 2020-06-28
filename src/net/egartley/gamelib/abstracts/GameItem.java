package net.egartley.gamelib.abstracts;

import org.newdawn.slick.Image;

public abstract class GameItem {

    public String id;
    public String displayName;
    public Image image;

    public GameItem(String id, String displayName, Image image) {
        this.id = id;
        this.displayName = displayName;
        this.image = image;
    }

    public boolean is(GameItem other) {
        return id.equals(other.id);
    }

}
