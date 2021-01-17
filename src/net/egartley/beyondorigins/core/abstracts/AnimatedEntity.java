package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
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
