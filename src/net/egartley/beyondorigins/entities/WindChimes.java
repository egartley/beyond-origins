package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.entities.AnimatedEntity;
import net.egartley.beyondorigins.engine.graphics.SpriteSheet;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.BoundaryPadding;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.engine.threads.DelayedEvent;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Animation;

/**
 * An object involved with the FH test boss
 */
public class WindChimes extends AnimatedEntity {

    // private boolean isIdle = true;
    private boolean swingAgain = true;
    private final int ANIMATION_THRESHOLD = 200;
    private final byte LEFT_SWING_ANIMATION = 0;
    private final byte RIGHT_SWING_ANIMATION = 1;
    private EntityBoundary mastBoundary;
    private EntityBoundary chimeBoundary;
    private EntityBoundary overheadBoundary;

    public WindChimes(int x, int y) {
        super("WindChimes", new SpriteSheet(
                Images.getImage(Images.WIND_CHIMES), 31, 54, 2, 5));
        isSectorSpecific = true;
        isDualRendered = false;
        isTraversable = false;
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
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(LEFT_SWING_ANIMATION)),
                ANIMATION_THRESHOLD + Util.randomInt(10, 100, true)));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(RIGHT_SWING_ANIMATION)),
                ANIMATION_THRESHOLD + Util.randomInt(10, 100, true)));
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
        mastBoundary = new EntityBoundary(this, sprite.width, sprite.height,
                new BoundaryPadding(-4, 0, -19, -27));
        overheadBoundary = new EntityBoundary(this, sprite.width, sprite.height,
                new BoundaryPadding(0, -4, -42, -4));
        chimeBoundary = new EntityBoundary(this, sprite.width, sprite.height,
                new BoundaryPadding(-12, -12, -29, 0));
        boundaries.add(mastBoundary);
        boundaries.add(overheadBoundary);
        boundaries.add(chimeBoundary);
        defaultBoundary = boundaries.get(2);
    }

}
