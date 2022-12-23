package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.AnimatedEntity;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

/**
 * An small animation that appears above an entity to convey an emotion
 */
public class EntityExpression extends AnimatedEntity {

    public byte type;
    public boolean isVisible = true;
    public Entity target;
    public static final byte ANGER = 3;
    public static final byte HEART = 9;
    public static final byte DISGUST = 1;
    public static final byte INTEREST = 0;
    public static final byte SADDNESS = 4;
    public static final byte THINKING = 6;
    public static final byte SURPRISE = 7;
    public static final byte HAPPINESS = 2;
    public static final byte CONFUSION = 5;
    public static final byte ATTENTION = 8;
    public static final short ANIMATIONSPEED_FAST = 626;
    public static final short ANIMATIONSPEED_SLOW = 313;
    public static final short ANIMATIONSPEED_NORMAL = 417;

    public EntityExpression(byte type, Entity target) {
        super("Expression");
        this.type = type;
        this.target = target;
        SpriteSheet sheet = getTemplateSpriteSheet(type);
        sheets.clear();
        sheets.add(sheet);
        sprites = sheet.sprites;
        sprite = sprites.get(0);
        animations.add(getTemplateAnimation());
        animation = animations.get(0);
        image = sprite.asImage();
    }

    private Animation getTemplateAnimation() {
        return new Animation(Util.getAnimationFrames(sprite), ANIMATIONSPEED_NORMAL);
    }

    private SpriteSheet getTemplateSpriteSheet(byte type) {
        return switch (type) {
            case CONFUSION ->
                    new SpriteSheet(Images.getImageFromPath(
                            Images.expressionPath + "confusion.png"), 18, 18, 1, 4);
            case ATTENTION ->
                    new SpriteSheet(Images.getImageFromPath(
                            Images.expressionPath + "attention.png"), 18, 18, 1, 4);
            case ANGER -> new SpriteSheet(Images.getImageFromPath(
                    Images.expressionPath + "anger.png"), 18, 18, 1, 2);
            case HEART -> new SpriteSheet(Images.getImageFromPath(
                    Images.expressionPath + "heart.png"), 18, 18, 1, 2);
            default -> new SpriteSheet(Images.getImageFromPath(
                    Images.path + "unknown.png"), 32, 32, 1, 1);
        };
    }

    @Override
    public void tick() {
        if (!isVisible) {
            return;
        }
        if (animation.isStopped()) {
            animation.start();
        }
        setPosition(Calculate.getCenter(target.x + (target.width / 2), sprite.width), target.y - 24);
        super.tick();
    }

    @Override
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
    public void setCollisions() {

    }

}
