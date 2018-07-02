package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

import java.awt.*;

public class Dummy extends StaticEntity {

    public Dummy(Sprite sprite) {
        super("Dummy");
        this.sprite = sprite;
        x = 470.0;
        y = 190.0;
        setBoundaries();
        setCollisions();

        isSectorSpecific = false;
        isDualRendered = false;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(sprite.getCurrentFrameAsBufferedImage(), (int) x, (int) y, null);
        drawDebug(graphics);
    }

    @Override
    public void tick() {

    }

    @Override
    public void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite.frameWidth, sprite.frameHeight, new BoundaryPadding(12)));
    }

    @Override
    protected void setCollisions() {

    }

}
