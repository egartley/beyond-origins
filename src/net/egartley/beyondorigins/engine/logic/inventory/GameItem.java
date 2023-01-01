package net.egartley.beyondorigins.engine.logic.inventory;

import org.newdawn.slick.Image;

public abstract class GameItem {

    public String id;
    public Image image;
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
