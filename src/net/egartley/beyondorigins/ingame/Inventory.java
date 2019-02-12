package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.graphics.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

import java.awt.*;

public class Inventory extends StaticEntity {

    private Color backgroundColor = new Color(0, 0, 0, 152);

    public Inventory(Sprite sprite) {
        super("Inventory", sprite);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        graphics.drawImage(sprite.toBufferedImage(), 289, 145, null);
    }

    @Override
    public void tick() {
    }

    @Override
    protected void setBoundaries() {

    }

    @Override
    protected void setCollisions() {

    }
}
