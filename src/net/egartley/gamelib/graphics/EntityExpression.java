package net.egartley.gamelib.graphics;

import net.egartley.beyondorigins.data.Images;
import net.egartley.gamelib.abstracts.AnimatedEntity;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.logic.math.Calculate;
import org.newdawn.slick.Graphics;

public class EntityExpression extends AnimatedEntity {

    public static final byte INTEREST = 0, DISGUST = 1, HAPPINESS = 2, ANGER = 3, SADDNESS = 4, CONFUSION = 5, THINKING = 6, SURPRISE = 7, ATTENTION = 8, HEART = 9;
    public static final short ANIMATIONSPEED_NORMAL = 417, ANIMATIONSPEED_FAST = 626, ANIMATIONSPEED_SLOW = 313;

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
        image = sprite.asImage();
    }

    private SpriteSheet getTemplateSpriteSheet(byte type) {
        switch (type) {
            case CONFUSION:
                return new SpriteSheet(Images.get(Images.expressionPath + "confusion.png"), 18, 18, 1, 4);
            case ATTENTION:
                return new SpriteSheet(Images.get(Images.expressionPath + "attention.png"), 18, 18, 1, 4);
            case ANGER:
                return new SpriteSheet(Images.get(Images.expressionPath + "anger.png"), 18, 18, 1, 2);
            case HEART:
                return new SpriteSheet(Images.get(Images.expressionPath + "heart.png"), 18, 18, 1, 2);
            default:
                return null;
        }
    }

    private Animation getTemplateAnimation(byte type) {
        return new Animation(getTemplateSpriteSheet(type).sprites.get(0), ANIMATIONSPEED_NORMAL);
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

    @Override
    protected void setCollisions() {

    }

    @Override
    protected void setInteractions() {

    }

}
