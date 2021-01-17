package net.egartley.beyondorigins.core.abstracts;

import org.newdawn.slick.Image;

public abstract class GameItem {

    public Image image;
    public String id;
    public String displayName;

    public GameItem(String id, String displayName, Image image) {
        this.id = id;
        this.displayName = displayName;
        this.image = image;
    }

    public boolean is(GameItem other) {
        return id.equals(other.id);
    }

}
