package net.egartley.gamelib.graphics;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.gamelib.logic.math.Calculate;
import net.egartley.gamelib.objects.AnimatedEntity;
import net.egartley.gamelib.objects.Entity;

import java.awt.*;

public class EntityExpression extends AnimatedEntity {

    public static final byte INTEREST = 0, DISGUST = 1, HAPPINESS = 2, ANGER = 3, SADDNESS = 4, CONFUSION = 5, THINKING = 6, SURPRISE = 7, CONCERN = 8, HEART = 9;
    // public static final short ANIMATIONSPEED_NORMAL = 0, ANIMATIONSPEED_FAST = 1, ANIMATIONSPEED_SLOW = 3;

    /**
     * Whether or not the expression is currently being shown
     */
    public boolean isVisible = true;

    public byte type;

    /**
     * The entity that the expression is rendered above
     */
    public Entity target;

    public EntityExpression(byte type, Entity target) {
        super("Expression");
        this.type = type;
        this.target = target;
        animations.add(getTemplateAnimation(type));
        animation = animations.get(0);
        sprite = animation.sprite;
    }

    private Animation getTemplateAnimation(byte type) {
        switch (type) {
            case CONFUSION:
                return new Animation(new SpriteSheet(ImageStore.get(ImageStore.EXPRESSION_CONFUSION), 18, 18, 1, 4).sprites.get(0), 417);
            case CONCERN:
                return new Animation(new SpriteSheet(ImageStore.get(ImageStore.EXPRESSION_CONCERN), 18, 18, 1, 4).sprites.get(0), 417);
            case ANGER:
                return new Animation(new SpriteSheet(ImageStore.get(ImageStore.EXPRESSION_ANGER), 18, 18, 1, 2).sprites.get(0), 417);
            case HEART:
                return new Animation(new SpriteSheet(ImageStore.get(ImageStore.EXPRESSION_HEART), 18, 18, 1, 2).sprites.get(0), 417);
            default:
                return null;
        }
    }

    public void tick() {
        if (!isVisible) {
            return;
        }
        if (!animation.clock.isRunning) {
            animation.start();
        }
        setPosition(Calculate.getCenter(target.x() + (target.sprite.width / 2), sprite.width), target.y() - 24);
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

}
