package net.egartley.beyondorigins.engine.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.engine.graphics.Sprite;
import net.egartley.beyondorigins.engine.graphics.SpriteSheet;
import net.egartley.beyondorigins.engine.interfaces.Interactable;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.List;

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
        setSprite(sprite);
    }

    public VisibleEntity(String name, SpriteSheet... sheets) {
        this(name, sheets[0].sprites.get(0));
        this.sheets.addAll(List.of(sheets));
        setSprites(0);
    }

    public void drawFirstLayer(Graphics graphics) {
        graphics.drawImage(firstLayer, x, y + secondLayer.getHeight());
    }

    public void drawSecondLayer(Graphics graphics) {
        graphics.drawImage(secondLayer, x, y);
        if (Game.debug) {
            drawDebug(graphics);
        }
    }

    public void setSprites(int index) {
        if (index < sheets.size()) {
            sprites = sheets.get(index).sprites;
            setSprite(0);
        } else {
            Debug.warning("Tried to get a sprite for \"" + this + "\" at an invalid index, " +
                    index + " (must be less than " + sprites.size() + ")");
        }
    }

    protected void setSprite(int index) {
        if (index < sprites.size() && index >= 0) {
            setSprite(sprites.get(index));
        } else {
            Debug.warning("Tried to get a sprite for \"" + this + "\" at an invalid index, " +
                    index + " (must be less than " + sprites.size() + ")");
        }
    }

    protected void setSprite(Sprite sprite) {
        if (sprite == null) {
            Debug.warning("Tried to set the sprite for " + this + " to null");
            return;
        }
        this.sprite = sprite;
        sprites.add(sprite);
        image = sprite.asImage();
        onSpriteChanged();
    }

    protected void onSpriteChanged() {
        boundaries.clear();
        setBoundaries();
        Collisions.endAllWith(this);
        Collisions.removeAllWith(this);
        setCollisions();
        if (this instanceof Interactable) {
            interactions.forEach(i -> i.collision.end());
            interactions.clear();
            ((Interactable) this).setInteractions();
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x, y);
        super.render(graphics);
    }

}