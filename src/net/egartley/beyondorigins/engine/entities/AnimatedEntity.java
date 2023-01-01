package net.egartley.beyondorigins.engine.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.engine.graphics.SpriteSheet;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public abstract class AnimatedEntity extends VisibleEntity {

    protected int leftAnimationIndex;
    protected int rightAnimationIndex;
    protected Animation animation;
    protected ArrayList<Animation> animations = new ArrayList<>();

    public AnimatedEntity(String name) {
        this(name, new SpriteSheet(Images.getImage(Images.UNKNOWN)));
    }

    public AnimatedEntity(String name, SpriteSheet... sheets) {
        super(name, sheets);
        setAnimations();
    }

    public abstract void setAnimations();

    @Override
    public void follow(Entity toFollow, EntityBoundary boundary, int tolerance) {
        super.follow(toFollow, boundary, tolerance);
        if (isMovingRightwards) {
            switchAnimation(rightAnimationIndex);
        } else if (isMovingLeftwards) {
            switchAnimation(leftAnimationIndex);
        }
    }

    protected void switchAnimation(int index) {
        if (index >= animations.size() || index < 0) {
            Debug.warning("Tried to switch to an animation at an invalid index");
            return;
        }
        // make sure the same animation isn't set again
        if (animations.indexOf(animation) != index) {
            animation.stop();
            animation = animations.get(index);
        }
    }

    @Override
    public void render(Graphics graphics) {
        animation.draw(x, y);
        if (Game.debug) {
            drawDebug(graphics);
        }
    }

}
