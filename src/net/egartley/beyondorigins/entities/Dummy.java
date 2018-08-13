package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

import java.awt.*;

public class Dummy extends StaticEntity {

    public Dummy(Sprite sprite) {
        super("Dummy", sprite);
        x = 470.0;
        y = 190.0;
        setBoundaries();
        setCollisions();

        isSectorSpecific = false;
        isDualRendered = false;
        speed = 2;
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
    }

    @Override
    public void tick() {
        follow(Entities.PLAYER);
        super.tick();
    }

    @Override
    public void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(4, 4, 3, 4)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    protected void setCollisions() {

    }

}
