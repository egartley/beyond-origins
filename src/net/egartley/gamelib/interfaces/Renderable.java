package net.egartley.gamelib.interfaces;

import java.awt.*;

public abstract class Renderable {

    private int x;
    private int y;

    protected abstract void render(Graphics graphics);

    public void setPosition(int x, int y) {
        x(x);
        y(y);
    }

    public void x(int x) {
        this.x = x;
    }

    public void y(int y) {
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

}
