package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.AnimatedEntity;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Animation;

public class WindChimes extends AnimatedEntity {

    private final int ANIMATION_THRESHOLD = 1200;

    public WindChimes(int x, int y) {
        super("WindChimes", new SpriteSheet(Images.get(Images.WIND_CHIMES), 42, 54, 1, 3));
        isSectorSpecific = true;
        isDualRendered = false;
        setPosition(x, y);
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(0)), ANIMATION_THRESHOLD));
        animation = animations.get(0);
    }

    @Override
    protected void setCollisions() {
        Collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.attackBoundary));
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(0)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    protected void setInteractions() {

    }

}
