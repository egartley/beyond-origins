package net.egartley.beyondorigins.graphics;

import net.egartley.beyondorigins.logic.math.Calculate;
import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Entity;

import java.awt.*;

public class EntityExpression extends AnimatedEntity {

    public static final short INTEREST = 0, DISGUST = 1, HAPPINESS = 2, ANGER = 3, SADDNESS = 4, CONFUSION = 5, THINKING = 6, SURPRISE = 7;
    public static final short ANIMATIONSPEED_NORMAL = 0, ANIMATIONSPEED_FAST = 1, ANIMATIONSPEED_SLOW = 3;

    private static Animation template_confusion;

    /**
     * Whether or not the expression is currently being shown
     */
    public boolean isVisible = true;

    public short type;

    /**
     *
     */
    public Entity target;

    public EntityExpression(short type, Entity target) {
        super("Expression");
        this.type = type;
        this.target = target;
        switch (type) {
            case CONFUSION:
                animations.add(template_confusion);
                break;
            default:
                break;
        }
        animation = animations.get(0);
        sprite = animation.sprite;
    }

    public static void init() {
        template_confusion = new Animation(new SpriteSheet(ImageStore.get(ImageStore.EXPRESSION_CONFUSION), 18, 18, 1, 4).sprites.get(0), (byte) 25);
    }

    public void tick() {
        if (!isVisible) {
            return;
        }
        x = Calculate.getCenter((int) target.x, target.sprite.width) - (sprite.width / 2.0);
        y = target.y - 24;
        super.tick();
    }

    public void render(Graphics graphics) {
        if (!isVisible) {
            return;
        }
        super.render(graphics);
    }

    @Override
    public void setAnimations() {

    }

    @Override
    protected void setBoundaries() {

    }

    @Override
    protected void setCollisions() {

    }
}
