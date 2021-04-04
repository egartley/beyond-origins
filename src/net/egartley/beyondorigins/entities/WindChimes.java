package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.AnimatedEntity;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.core.threads.DelayedEvent;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Animation;

public class WindChimes extends AnimatedEntity {

    // private boolean isIdle = true;
    private boolean swingAgain = true;

    private final int ANIMATION_THRESHOLD = 200;
    private final byte LEFT_SWING_ANIMATION = 0, RIGHT_SWING_ANIMATION = 1;

    public WindChimes(int x, int y) {
        super("WindChimes", new SpriteSheet(Images.get(Images.WIND_CHIMES), 42, 54, 2, 5));
        isSectorSpecific = true;
        isDualRendered = false;
        setPosition(x, y);
    }

    @Override
    public void tick() {
        super.tick();

        // simulate wind with random animation delays and lengths
        if (animation.isStopped() && swingAgain) {
            swingAgain = false;
            new DelayedEvent(0.1D * Util.randomInt(4, 8, true)) {
                @Override
                public void onFinish() {
                    animation = animations.get(Util.fiftyFifty() ? LEFT_SWING_ANIMATION : RIGHT_SWING_ANIMATION);
                    animation.restart();
                    swingAgain = true;
                }
            }.start();
        }
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(LEFT_SWING_ANIMATION)), ANIMATION_THRESHOLD + Util.randomInt(10, 100, true)));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(RIGHT_SWING_ANIMATION)), ANIMATION_THRESHOLD + Util.randomInt(10, 100, true)));
        animation = animations.get(LEFT_SWING_ANIMATION);
        for (Animation a : animations) {
            a.setLooping(false);
        }
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
