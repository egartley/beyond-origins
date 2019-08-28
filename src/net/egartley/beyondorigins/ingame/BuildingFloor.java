package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.gamelib.abstracts.Renderable;
import net.egartley.gamelib.interfaces.Tickable;
import net.egartley.gamelib.logic.math.Calculate;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BuildingFloor extends Renderable implements Tickable {

    public int number;
    public Building parent;
    public BufferedImage image;

    public BuildingFloor(int number, Building parent) {
        this.number = number;
        this.parent = parent;
        image = ImageStore.get("resources/images/buildings/floors/" + parent.id + "_" + number + ".png");
        setPosition(Calculate.getCenteredX(image.getWidth()), Calculate.getCenteredY(image.getHeight()));
    }

    public void onPlayerEnter() {

    }

    public void onPlayerLeave() {

    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x(), y(), null);
    }

    @Override
    public void tick() {

    }
}
