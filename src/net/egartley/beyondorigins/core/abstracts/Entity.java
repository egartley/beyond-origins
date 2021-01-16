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
import net.egartley.beyondorigins.core.logic.inventory.EntityInventory;
import org.newdawn.slick.*;

import java.util.ArrayList;

/**
 * Anything that is rendered with a {@link Sprite} at a specified location
 *
 * @see AnimatedEntity
 * @see StaticEntity
 */
public abstract class Entity extends Renderable implements Tickable {

    public static final byte DIRECTION_UP = 1;
    public static final byte DIRECTION_DOWN = 2;
    public static final byte DIRECTION_LEFT = 3;
    public static final byte DIRECTION_RIGHT = 4;

    /**
     * The entity's sprites
     */
    protected ArrayList<Sprite> sprites = new ArrayList<>();
    /**
     * The entity's sprite sheets
     */
    protected ArrayList<SpriteSheet> sheets = new ArrayList<>();
    /**
     * Collection of the entity's boundaries
     */
    public ArrayList<EntityBoundary> boundaries = new ArrayList<>();
    /**
     * Collection of the entity's interactions
     */
    public ArrayList<EntityEntityInteraction> interactions = new ArrayList<>();

    public EntityInventory inventory;

    /**
     * The sprite to use while rendering
     */
    public Sprite sprite;
    /**
     * The buffered image to use when rendering (default)
     */
    protected Image image;
    /**
     * If {@link #isDualRendered} is true, render this before the player
     * ("below")
     */
    protected Image firstLayer;
    /**
     * If {@link #isDualRendered} is true, render this after the player
     * ("above")
     */
    protected Image secondLayer;
    /**
     * The most recent collision that has occurred for the entity. If no
     * collisions have occurred within the entity's lifetime, this will be
     * null
     */
    public EntityEntityCollision lastCollision = null;
    /**
     * The most recent collision event to have occurred. This will be null
     * if no collision event has yet to take place
     */
    public EntityEntityCollisionEvent lastCollisionEvent = null;
    /**
     * The boundary to use if not specified
     */
    public EntityBoundary defaultBoundary = null;
    /**
     * Less-than-one change in the x-coordinate, used for non-integer speeds
     */
    public double deltaX;
    /**
     * Less-than-one change in the y-coordinate, used for non-integer speeds
     */
    public double deltaY;
    /**
     * The entity's speed (magnitude of its location change when moving)
     */
    public double speed;
    /**
     * The entity's unique identification number. Use {@link #name} for
     * user-friendly identification
     */
    public int uuid;
    /**
     * Amount of damage the entity can take before dying
     */
    public int health;
    /**
     * Maximum amount of health the entity can have
     */
    public int maximumHealth;
    /**
     * Whether or not the entity is animated
     */
    public boolean isAnimated;
    /**
     * Whether or not other entities, mainly the player, can walk over it
     * or not
     */
    public boolean isTraversable;
    /**
     * Whether or not the entity is currently collided with another entity
     */
    public boolean isCollided;
    /**
     * Whether or not the entity is "allowed" to collide with others
     */
    public boolean canCollide = true;
    /**
     * Whether or not the entity is currently moving upwards
     */
    protected boolean isMovingUpwards;
    /**
     * Whether or not the entity is currently moving downwards
     */
    protected boolean isMovingDownwards;
    /**
     * Whether or not the entity is currently moving leftwards
     */
    protected boolean isMovingLeftwards;
    /**
     * Whether or not the entity is currently moving rightwards
     */
    protected boolean isMovingRightwards;
    /**
     * Whether or not the entity is allowed to move upwards
     */
    public boolean isAllowedToMoveUpwards = true;
    /**
     * Whether or not the entity is allowed to move downwards
     */
    public boolean isAllowedToMoveDownwards = true;
    /**
     * Whether or not the entity is allowed to move leftwards
     */
    public boolean isAllowedToMoveLeftwards = true;
    /**
     * Whether or not the entity is allowed to move rightwards
     */
    public boolean isAllowedToMoveRightwards = true;
    /**
     * Whether or not the entity has two different "layers" that are
     * rendered before and after the player
     *
     * @see #drawFirstLayer(Graphics)
     * @see #drawSecondLayer(Graphics)
     */
    public boolean isDualRendered;
    /**
     * Whether or not the entity is "bound" to, or only exists in, a
     * specific map sector
     */
    public boolean isSectorSpecific;
    /**
     * Human-readable identifier for the entity (not unique)
     */
    public String name;

    private static final Font debugFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11), true);
    private static final Color nameTagBackgroundColor = new Color(0, 0, 0, 128);
    private static final Color healthBarColor = new Color(0, 179, 0);

    private final int healthBarWidth = 64;
    private int nameTagWidth;
    /**
     * Whether or not font metrics have been initialized. Since {@link
     * #render(Graphics)} is called about 60 times a second, and the
     * resulting font metrics object will always be the same, there is no
     * need to keep re-computing it each time {@link #render(Graphics)} is
     * called
     */
    private boolean setFontMetrics;

    Entity(String name) {
        this(name, (Sprite) null);
    }

    Entity(String name, Sprite sprite) {
        inventory = new EntityInventory(this);
        uuid = Util.randomInt(999999, 100000, true);
        this.name = name;
        if (sprite != null) {
            setSprite(sprite);
            setBoundaries();
        }
        speed = 1.0;
        health = 50;
        maximumHealth = health;
        setCollisions();
        setInteractions();
    }

    Entity(String name, SpriteSheet sheet) {
        this(name, sheet.sprites.get(0));
        sprites = sheet.sprites;
        sheets.add(sheet);
    }

    /**
     * Renders the entity, using {@link #image}, at ({@link #x()}, {@link #y()})
     */
    public void render(Graphics graphics) {
        graphics.drawImage(image, x(), y());
        if (Game.debug) {
            drawDebug(graphics);
        }
    }

    /**
     * Draws the first "layer", assuming {@link #isDualRendered} is true (below the player)
     */
    public void drawFirstLayer(Graphics graphics) {
        graphics.drawImage(firstLayer, x(), y() + secondLayer.getHeight());
    }

    /**
     * Draws the second "layer", assuming {@link #isDualRendered} is true (above the player)
     */
    public void drawSecondLayer(Graphics graphics) {
        graphics.drawImage(secondLayer, x(), y());
        if (Game.debug) {
            drawDebug(graphics);
        }
    }

    /**
     * Renders debug information, such as the entity's boundaries and "name tag"
     */
    public void drawDebug(Graphics graphics) {
        drawBoundaries(graphics);
        drawNameTag(graphics);
        if (this instanceof Damageable) {
            drawHealthBar(graphics);
        }
    }

    /**
     * Draws the entity's "name tag", which is {@link #toString()}
     */
    private void drawNameTag(Graphics graphics) {
        if (!setFontMetrics) {
            nameTagWidth = debugFont.getWidth(toString()) + 8;
            setFontMetrics = true;
        }
        int nameX = (x() + (sprite.width / 2)) - nameTagWidth / 2;
        int nameY = y() - 18;

        graphics.setColor(nameTagBackgroundColor);
        graphics.setFont(debugFont);

        graphics.fillRect(nameX, nameY, nameTagWidth, 18);
        graphics.setColor(Color.white);
        graphics.drawString(toString(), nameX + 5, nameY + 16 - debugFont.getLineHeight());
    }

    /**
     * Shows the entity's health above them
     */
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

    /**
     * Draws all of the entity's boundaries
     */
    private void drawBoundaries(Graphics graphics) {
        boundaries.forEach(boundary -> boundary.draw(graphics));
    }

    /**
     * Ticks boundaries and interactions
     *
     * @see #boundaries
     */
    @Override
    public void tick() {
        boundaries.forEach(EntityBoundary::tick);
        interactions.forEach(EntityEntityInteraction::tick);
    }

    /**
     * Sets the entity's x and y coordinates, as well as updating the coordinate deltas
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @see #deltaX
     * @see #deltaY
     */
    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        deltaX = x;
        deltaY = y;
    }

    @Override
    public void x(int x) {
        x(x, true);
    }

    @Override
    public void y(int y) {
        y(y, true);
    }

    public void x(int x, boolean setDelta) {
        super.x(x);
        if (setDelta) {
            deltaX = x;
        }
    }

    public void y(int y, boolean setDelta) {
        super.y(y);
        if (setDelta) {
            deltaY = y;
        }
    }

    public void onSectorEnter(MapSector entering) {

    }

    public void onSectorLeave(MapSector leaving) {

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
     * Called whenever the entity moves
     *
     * @param direction Which direction the entity moved in
     * @see #DIRECTION_UP
     * @see #DIRECTION_DOWN
     * @see #DIRECTION_LEFT
     * @see #DIRECTION_RIGHT
     * @see #move(byte)
     */
    protected void onMove(byte direction) {

    }

    /**
     * Changes the entity's location ({@link #x()} and {@link #y()}) at a rate
     * of {@link #speed} per call. Since there is no boundary parameter,
     * the entity's {@link #defaultBoundary} will be used
     */
    protected void move(byte direction) {
        move(direction, defaultBoundary);
    }

    /**
     * Returns whether or not the entity is to the "right" of the other
     * entity
     */
    private boolean isRightOf(Entity e) {
        return x() > e.x();
    }

    /**
     * Returns whether or not the entity is to the "left" of the other
     * entity
     */
    private boolean isLeftOf(Entity e) {
        return !isRightOf(e);
    }

    /**
     * Returns whether or not the entity is "above" the other entity
     */
    private boolean isAbove(Entity e) {
        return y() < e.y();
    }

    /**
     * Returns whether or not the entity is "below" the other entity
     */
    private boolean isBelow(Entity e) {
        return !isAbove(e);
    }

    /**
     * Sets the entity's boundaries
     *
     * @see #boundaries
     */
    protected abstract void setBoundaries();

    /**
     * Sets the entity's collisions
     *
     * @see #boundaries
     */
    public abstract void setCollisions();

    /**
     * Sets the entity's interactions
     *
     * @see #interactions
     */
    protected abstract void setInteractions();

    private void onSpriteChanged() {
        image = sprite.asImage();
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
     * Sets the current sprite
     *
     * @param index The index in {@link #sprites}
     * @see #sprite
     */
    public void setSprite(int index, boolean update) {
        if (index < sprites.size()) {
            sprite = sprites.get(index);
            if (update) {
                onSpriteChanged();
            }
        } else {
            Debug.warning("Tried to get a sprite for \"" + this + "\" at an valid index, " + index + " (must be less than " + sprites.size() + ")");
        }
    }

    public void setSprite(Sprite sprite) {
        if (sprite == null) {
            Debug.warning("Tried to set the sprite for " + this + " to null");
            return;
        }
        this.sprite = sprite;
        sprites.clear();
        sprites.add(sprite);
        image = sprite.asImage();
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

    public String toString() {
        return name + "#" + uuid;
    }

}
