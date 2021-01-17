package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.interfaces.Damageable;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.core.logic.interaction.EntityEntityInteraction;
import org.newdawn.slick.*;

import java.util.ArrayList;

public abstract class Entity extends Renderable implements Tickable {

    private final int healthBarWidth = 64;
    private static final Color healthBarColor = new Color(0, 179, 0);
    private static final Color nameTagBackgroundColor = new Color(0, 0, 0, 128);
    private static final Font debugFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11), true);

    protected Image image;
    protected Image firstLayer;
    protected Image secondLayer;
    protected ArrayList<Sprite> sprites = new ArrayList<>();
    protected ArrayList<SpriteSheet> sheets = new ArrayList<>();
    protected boolean isMovingUpwards;
    protected boolean isMovingDownwards;
    protected boolean isMovingLeftwards;
    protected boolean isMovingRightwards;

    public String name;
    public Sprite sprite;
    public EntityBoundary defaultBoundary = null;
    public EntityEntityCollision lastCollision = null;
    public EntityEntityCollisionEvent lastCollisionEvent = null;
    public ArrayList<EntityBoundary> boundaries = new ArrayList<>();
    public ArrayList<EntityEntityInteraction> interactions = new ArrayList<>();
    public int uuid;
    public int health;
    public double deltaX;
    public double deltaY;
    public double speed;
    public int maximumHealth;
    public boolean isCollided;
    public boolean isTraversable;
    public boolean isDualRendered;
    public boolean isSectorSpecific;
    public boolean canCollide = true;
    public boolean isAllowedToMoveUpwards = true;
    public boolean isAllowedToMoveDownwards = true;
    public boolean isAllowedToMoveLeftwards = true;
    public boolean isAllowedToMoveRightwards = true;
    public static final byte DIRECTION_UP = 1;
    public static final byte DIRECTION_DOWN = 2;
    public static final byte DIRECTION_LEFT = 3;
    public static final byte DIRECTION_RIGHT = 4;

    public Entity(String name) {
        this(name, (Sprite) null);
    }

    public Entity(String name, Sprite sprite) {
        this.name = name;
        if (sprite != null) {
            setSprite(sprite, false);
        }
        setBoundaries();
        setCollisions();
        setInteractions();
        speed = 1.0;
        health = 50;
        maximumHealth = health;
        uuid = Util.randomInt(999999, 100000, true);
    }

    public Entity(String name, SpriteSheet sheet) {
        this(name, sheet.sprites.get(0));
        sprites = sheet.sprites;
        sheets.add(sheet);
    }

    /**
     * Renders the entity, using {@link #image}, at ({@link #x()}, {@link #y()})
     */
    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x(), y());
        if (Game.debug) {
            drawDebug(graphics);
        }
    }

    /**
     * Calls {@link EntityBoundary#tick()} and {@link EntityEntityInteraction#tick()}
     *
     * @see #boundaries
     * @see #interactions
     */
    @Override
    public void tick() {
        boundaries.forEach(EntityBoundary::tick);
        interactions.forEach(EntityEntityInteraction::tick);
    }

    /**
     * Renders debug information, such as the entity's boundaries, name and health if applicable
     */
    public void drawDebug(Graphics graphics) {
        drawBoundaries(graphics);
        drawNameTag(graphics);
        if (this instanceof Damageable) {
            drawHealthBar(graphics);
        }
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

    /**
     * Draws the entity's name and UUID above it, which is {@link #toString()}
     */
    private void drawNameTag(Graphics graphics) {
        int width = debugFont.getWidth(toString()) + 8;
        int nameX = (x() + (sprite.width / 2)) - width / 2;
        int nameY = y() - 18;
        graphics.setColor(nameTagBackgroundColor);
        graphics.setFont(debugFont);
        graphics.fillRect(nameX, nameY, width, 18);
        graphics.setColor(Color.white);
        graphics.drawString(toString(), nameX + 5, nameY + 16 - debugFont.getLineHeight());
    }

    private void drawHealthBar(Graphics graphics) {
        int baseX = (x() + (sprite.width / 2)) - healthBarWidth / 2;
        int baseY = y() - 30;
        graphics.setColor(Color.black);
        graphics.fillRect(baseX - 1, baseY - 1, healthBarWidth + 2, 8);
        graphics.setColor(Color.red);
        graphics.fillRect(baseX, baseY, healthBarWidth, 6);
        graphics.setColor(healthBarColor);
        graphics.fillRect(baseX, baseY, health * (healthBarWidth) / (maximumHealth), 6);
    }

    private void drawBoundaries(Graphics graphics) {
        boundaries.forEach(boundary -> boundary.render(graphics));
    }

    protected abstract void setCollisions();

    protected abstract void setBoundaries();

    protected abstract void setInteractions();

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        deltaX = x;
        deltaY = y;
    }

    /**
     * Sets {@link #sprites} to {@link SpriteSheet#sprites} from {@link #sheets} at the given index
     *
     * @param index The index in {@link #sheets}
     * @see #sheets
     * @see #sprites
     */
    public void setSprites(int index) {
        if (index < sheets.size()) {
            sprites = sheets.get(index).sprites;
            setSprite(0, true);
        } else {
            Debug.warning("Tried to get a sprite for \"" + this + "\" at an invalid index, " + index + " (must be less than " + sprites.size() + ")");
        }
    }

    /**
     * Sets {@link #sprite} from the given index in {@link #sprites}
     *
     * @param index The index in {@link #sprites}
     * @param update Whether or not to call {@link #onSpriteChanged()}
     */
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

    @Override
    public void x(int x) {
        x(x, true);
    }

    @Override
    public void y(int y) {
        y(y, true);
    }

    private void x(int x, boolean setDelta) {
        super.x(x);
        if (setDelta) {
            deltaX = x;
        }
    }

    private void y(int y, boolean setDelta) {
        super.y(y);
        if (setDelta) {
            deltaY = y;
        }
    }

    /**
     * Sets {@link #image}, then clears and resets any animations, boundaries, collisions or interactions
     */
    private void onSpriteChanged() {
        if (this instanceof AnimatedEntity) {
            ((AnimatedEntity) this).animations.clear();
            ((AnimatedEntity) this).setAnimations();
        }
        boundaries.clear();
        setBoundaries();
        Collisions.endWith(this);
        setCollisions();
        interactions.forEach(i -> i.collision.end());
        interactions.clear();
        setInteractions();
    }

    /**
     * Called whenever the entity moves
     *
     * @param direction Which direction the entity moved in
     * @see #move(byte)
     * @see #DIRECTION_UP
     * @see #DIRECTION_DOWN
     * @see #DIRECTION_LEFT
     * @see #DIRECTION_RIGHT
     */
    protected void onMove(byte direction) {

    }

    /**
     * Updates the entity's location by {@link #speed}, unless the
     * specified boundary is outside of the game's window
     */
    private void move(byte direction, EntityBoundary boundary) {
        isMovingUpwards = false;
        isMovingDownwards = false;
        isMovingLeftwards = false;
        isMovingRightwards = false;
        if (direction == DIRECTION_UP && !isAllowedToMoveUpwards)
            return;
        if (direction == DIRECTION_DOWN && !isAllowedToMoveDownwards)
            return;
        if (direction == DIRECTION_LEFT && !isAllowedToMoveLeftwards)
            return;
        if (direction == DIRECTION_RIGHT && !isAllowedToMoveRightwards)
            return;
        if (direction == -1)
            return;
        switch (direction) {
            case DIRECTION_UP:
                if (boundary.top <= 0) {
                    break; // top of window
                }
                isMovingUpwards = true;
                deltaY -= speed;
                y(y() - (int) Math.abs(deltaY - y()), false);
                onMove(DIRECTION_UP);
                break;
            case DIRECTION_DOWN:
                if (boundary.bottom >= Game.WINDOW_HEIGHT) {
                    break; // bottom of window
                }
                isMovingDownwards = true;
                deltaY += speed;
                y(y() + (int) Math.abs(deltaY - y()), false);
                onMove(DIRECTION_DOWN);
                break;
            case DIRECTION_LEFT:
                if (boundary.left <= 0) {
                    break; // left side of window
                }
                isMovingLeftwards = true;
                deltaX -= speed;
                x(x() - (int) Math.abs(deltaX - x()), false);
                onMove(DIRECTION_LEFT);
                break;
            case DIRECTION_RIGHT:
                if (boundary.right >= Game.WINDOW_WIDTH) {
                    break; // right side of window
                }
                isMovingRightwards = true;
                deltaX += speed;
                x(x() + (int) Math.abs(deltaX - x()), false);
                onMove(DIRECTION_RIGHT);
                break;
            default:
                break;
        }
    }

    /**
     * Changes the entity's location ({@link #x()} and {@link #y()}) at a rate
     * of {@link #speed} per call. Since there is no boundary parameter,
     * the entity's {@link #defaultBoundary} will be used in calculating where it is
     */
    protected void move(byte direction) {
        move(direction, defaultBoundary);
    }

    /**
     * Allows the entity to move in all directions
     *
     * @see #isAllowedToMoveUpwards
     * @see #isAllowedToMoveDownwards
     * @see #isAllowedToMoveLeftwards
     * @see #isAllowedToMoveRightwards
     */
    protected void allowAllMovement() {
        isAllowedToMoveUpwards = true;
        isAllowedToMoveDownwards = true;
        isAllowedToMoveLeftwards = true;
        isAllowedToMoveRightwards = true;
    }

    /**
     * Returns whether or not the entity is to the "right" of the other
     * entity (larger x-coordinate)
     */
    public boolean isRightOf(Entity e) {
        return x() > e.x();
    }

    /**
     * Returns whether or not the entity is to the "left" of the other
     * entity (smaller x-coordinate)
     */
    public boolean isLeftOf(Entity e) {
        return !isRightOf(e);
    }

    /**
     * Returns whether or not the entity is "above" the other entity
     * (smaller y-coordinate)
     */
    public boolean isAbove(Entity e) {
        return y() < e.y();
    }

    /**
     * Returns whether or not the entity is "below" the other entity
     * (larger y-coordinate)
     */
    public boolean isBelow(Entity e) {
        return !isAbove(e);
    }

    public String toString() {
        return name + "#" + uuid;
    }

}
