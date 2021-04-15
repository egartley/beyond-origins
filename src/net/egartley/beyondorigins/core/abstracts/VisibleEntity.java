package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.ArrayList;

public abstract class VisibleEntity extends Entity {

    protected Image image;
    protected Image firstLayer;
    protected Image secondLayer;
    protected ArrayList<Sprite> sprites = new ArrayList<>();
    protected ArrayList<SpriteSheet> sheets = new ArrayList<>();

    public boolean isDualRendered;
    public Sprite sprite;

    public VisibleEntity(String name) {
        super(name, 0, 0);
    }

    public VisibleEntity(String name, Sprite sprite) {
        super(name, sprite.width, sprite.height);
        setSprite(sprite, false);
        setBoundaries();
        setCollisions();
        setInteractions();
    }

    public VisibleEntity(String name, SpriteSheet sheet) {
        this(name, sheet.sprites.get(0));
        sprites = sheet.sprites;
        sheets.add(sheet);
    }

    public void drawFirstLayer(Graphics graphics) {
        graphics.drawImage(firstLayer, x(), y() + secondLayer.getHeight());
    }

    public void drawSecondLayer(Graphics graphics) {
        graphics.drawImage(secondLayer, x(), y());
        if (Game.debug) {
            drawDebug(graphics);
        }
    }

    public void setSprites(int index) {
        if (index < sheets.size()) {
            sprites = sheets.get(index).sprites;
            setSprite(0, true);
        } else {
            Debug.warning("Tried to get a sprite for \"" + this + "\" at an invalid index, " + index + " (must be less than " + sprites.size() + ")");
        }
    }

    protected void setSprite(int index, boolean update) {
        if (index < sprites.size() && index >= 0) {
            setSprite(sprites.get(index), update);
        } else {
            Debug.warning("Tried to get a sprite for \"" + this + "\" at an invalid index, " + index + " (must be less than " + sprites.size() + ")");
        }
    }

    protected void setSprite(Sprite sprite, boolean update) {
        if (sprite == null) {
            Debug.warning("Tried to set the sprite for " + this + " to null");
            return;
        }
        this.sprite = sprite;
        sprites.add(sprite);
        image = sprite.asImage();
        if (update) {
            onSpriteChanged();
        }
    }

    protected void onSpriteChanged() {
        /*if (this instanceof AnimatedEntity) {
            ((AnimatedEntity) this).animations.clear();
            ((AnimatedEntity) this).setAnimations();
        }*/
        boundaries.clear();
        setBoundaries();
        Collisions.endWith(this);
        setCollisions();
        interactions.forEach(i -> i.collision.end());
        interactions.clear();
        setInteractions();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x(), y());
        super.render(graphics);
    }

}
