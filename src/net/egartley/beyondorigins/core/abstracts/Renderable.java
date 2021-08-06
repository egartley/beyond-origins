package net.egartley.beyondorigins.core.abstracts;

import org.newdawn.slick.Graphics;

public abstract class Renderable {

    public int x;
    public int y;

    public abstract void render(Graphics graphics);

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
