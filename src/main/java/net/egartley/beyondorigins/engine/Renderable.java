package net.egartley.beyondorigins.engine;

import org.newdawn.slick.Graphics;

public abstract class Renderable {

    public int x, y, width, height;

    public Renderable(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void render(Graphics graphics) {

    }

}
