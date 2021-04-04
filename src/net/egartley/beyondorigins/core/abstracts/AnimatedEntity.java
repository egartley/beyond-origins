package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public abstract class AnimatedEntity extends Entity {

    protected Animation animation;
    protected ArrayList<Animation> animations = new ArrayList<>();

    public AnimatedEntity(String id) {
        super(id, (Sprite) null);
        setAnimations();
    }

    public AnimatedEntity(String id, SpriteSheet sheet) {
        super(id, sheet);
        setAnimations();
    }

    public abstract void setAnimations();

    protected void follow(Entity toFollow, int leftIndex, int rightIndex) {
        follow(toFollow, leftIndex, rightIndex, defaultBoundary);
    }

    protected void follow(Entity toFollow, int leftIndex, int rightIndex, EntityBoundary boundary) {
        follow(toFollow, leftIndex, rightIndex, boundary, 1);
    }

    protected void follow(Entity toFollow, int leftIndex, int rightIndex, EntityBoundary boundary, int tolerance) {
        if (!Calculate.isWithinToleranceOf(this.x(), toFollow.x(), tolerance)) {
            if (isMovingRightwards && this.isRightOf(toFollow)) {
                animation.stop();
                animation = animations.get(leftIndex);
                isMovingRightwards = false;
                isMovingLeftwards = true;
            } else if (isMovingLeftwards && this.isLeftOf(toFollow)) {
                animation.stop();
                animation = animations.get(rightIndex);
                isMovingLeftwards = false;
                isMovingRightwards = true;
            }
        } else {
            isMovingLeftwards = false;
            isMovingRightwards = false;
        }
        if (!Calculate.isWithinToleranceOf(this.y(), toFollow.y(), tolerance)) {
            if (this.isBelow(toFollow)) {
                isMovingDownwards = false;
                isMovingUpwards = true;
            } else if (this.isAbove(toFollow)) {
                isMovingDownwards = true;
                isMovingUpwards = false;
            }
        } else {
            isMovingUpwards = false;
            isMovingDownwards = false;
        }
        if (isMovingRightwards) {
            move(DIRECTION_RIGHT, boundary, false);
        } else if (isMovingLeftwards) {
            move(DIRECTION_LEFT, boundary, false);
        }
        if (isMovingDownwards) {
            move(DIRECTION_DOWN, boundary, false);
        } else if (isMovingUpwards) {
            move(DIRECTION_UP, boundary, false);
        }
    }

    /**
     * Changes {@link #animation}
     *
     * @param i The index of the animation to switch to in {@link #animations}
     */
    protected void switchAnimation(int i) {
        if (i >= animations.size()) {
            Debug.warning("Tried to switch to an animation at an invalid index");
            return;
        }
        if (animations.indexOf(animation) != i) {
            // this prevents the same animation being set again
            animation.stop();
            animation = animations.get(i);
        }
    }

    @Override
    public void render(Graphics graphics) {
        animation.draw(x(), y());
        if (Game.debug) {
            drawDebug(graphics);
        }
    }

}
