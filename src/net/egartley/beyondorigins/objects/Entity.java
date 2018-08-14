package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.entities.EntityStore;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.Boundary;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.logic.math.Calculate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * An object or character that can rendered with a {@link Sprite} at a specified location
 *
 * @see AnimatedEntity
 * @see StaticEntity
 */
public abstract class Entity {

    public static final byte UP = 1;
    public static final byte DOWN = 2;
    public static final byte LEFT = 3;
    public static final byte RIGHT = 4;

    /**
     * The entity's sprites
     */
    protected ArrayList<Sprite> sprites;
    /**
     * The entity's collisions
     */
    public ArrayList<EntityEntityCollision> collisions;
    /**
     * The entity's collisions, but only those that are currently collided
     *
     * @see EntityEntityCollision#isCollided
     */
    // "concurrent" may not be the best word to use here, but it sounds cool, so...
    public ArrayList<EntityEntityCollision> concurrentCollisions;
    /**
     * Collection of the entity's boundaries
     */
    public ArrayList<EntityBoundary> boundaries;
    /**
     * The sprite to use while rendering
     */
    public Sprite sprite;
    /**
     * If {@link #isDualRendered} is true, render this before the player ("below")
     */
    protected BufferedImage firstLayer;
    /**
     * If {@link #isDualRendered} is true, render this after the player ("above")
     */
    protected BufferedImage secondLayer;
    /**
     * The most recent collision that has occurred for the entity. If no collisions have occurred within the entity's
     * lifetime, this will be null
     */
    public EntityEntityCollision lastCollision = null;
    /**
     * The most recent collision event to have occurred. This will be null if no collision event has yet to take place
     */
    public EntityEntityCollisionEvent lastCollisionEvent = null;
    /**
     *
     */
    public EntityBoundary defaultBoundary = null;
    /**
     * The entity's x-axis coordinate (absolute)
     */
    public double x;
    /**
     * The entity's y-axis coordinate (absolute)
     */
    public double y;
    /**
     * The entity's speed
     */
    public double speed;
    /**
     * The entity's unique identification number. Use {@link #id} for user-friendly identification
     */
    public int uuid;
    /**
     * Whether or not the entity is animated
     */
    boolean isAnimated;
    /**
     * Whether or not the entity is static (no animation)
     */
    public boolean isStatic;
    /**
     * Whether or not the entity is currently collided with another entity
     */
    public boolean isCollided;
    /**
     * Whether or not the entity is currently moving upwards
     */
    public boolean isMovingUpwards = false;
    /**
     * Whether or not the entity is currently moving downwards
     */
    public boolean isMovingDownwards = false;
    /**
     * Whether or not the entity is currently moving leftwards
     */
    public boolean isMovingLeftwards = false;
    /**
     * Whether or not the entity is currently moving rightwards
     */
    public boolean isMovingRightwards = false;
    /**
     * Whether or not the entity has two different "layers" that are rendered before and after the player
     */
    protected boolean isDualRendered;
    /**
     * Whether or not the entity is currently registered in the entity store
     */
    public boolean isRegistered;
    /**
     * Whether or not the entity is "bound" to, or only exists in, a specific map sector
     */
    public boolean isSectorSpecific;
    /**
     * Human-readable identifier for the entity
     */
    private String id;
    /**
     * The entity's generic, non-specific name, such as "Rock" or "Tree"
     */
    private String name;

    // the font and color are static because they're always the same
    private static Font nameTagFont = new Font("Arial", Font.PLAIN, 11);
    private static Color nameTagBackgroundColor = new Color(0, 0, 0, 128);

    private int nameTagWidth;
    private int entityWidth;
    private int nameX;
    private int nameY;
    /**
     * Whether or not font metrics have been initialized. Since {@link #render(Graphics)} is called about 60 times a second, and the resulting font metrics object will always be the same, there is no need to keep re-computing it each time {@link #render(Graphics)} is called, only the first
     */
    private boolean setFontMetrics = false;

    /**
     * Creates a new entity with a randomly generated UUID, an initial speed of <code>1.0</code>, then adds it to the entity store
     */
    Entity(String id) {
        generateUUID();
        this.id = id;
        speed = 1.0;
        boundaries = new ArrayList<>();
        collisions = new ArrayList<>();
        concurrentCollisions = new ArrayList<>();
        EntityStore.register(this);
    }

    /**
     * Renders the entity, using {@link #sprite}, at ({@link #x}, {@link #y})
     */
    public void render(Graphics graphics) {
        graphics.drawImage(sprite.asBufferedImage(0), (int) x, (int) y, null);
        drawDebug(graphics);
    }

    /**
     * Draws the first "layer" if {@link #isDualRendered} is true (below the player)
     */
    public void drawFirstLayer(Graphics graphics) {
        graphics.drawImage(firstLayer, (int) x, (int) y + secondLayer.getHeight(), null);
    }

    /**
     * Draws the second "layer" if {@link #isDualRendered} is true (above the player)
     */
    public void drawSecondLayer(Graphics graphics) {
        graphics.drawImage(secondLayer, (int) x, (int) y, null);
        drawDebug(graphics);
    }

    /**
     * Renders debug information, such as the entity's boundaries and "name tag"
     *
     * @see Game#debug
     */
    protected void drawDebug(Graphics graphics) {
        if (Game.debug) {
            drawBoundaries(graphics);
            drawNameTag(graphics);
        }
    }

    /**
     * Draws the entity's "name tag", which displays its {@link #id} and {@link #uuid}
     */
    private void drawNameTag(Graphics graphics) {
        if (!setFontMetrics) {
            name = toString();
            nameTagWidth = graphics.getFontMetrics(nameTagFont).stringWidth(name) + 8; // 4px padding on both sides, so add double of 4
            entityWidth = sprite.width;
            // don't initialize again, not needed
            setFontMetrics = true;
        }
        nameX = Calculate.horizontalCenter((int) x, entityWidth) - Calculate.horizontalCenter(0, nameTagWidth);
        nameY = (int) y - 18;

        graphics.setColor(nameTagBackgroundColor);
        graphics.setFont(nameTagFont);

        graphics.fillRect(nameX, nameY, nameTagWidth, 18);
        graphics.setColor(Color.WHITE);
        graphics.drawString(name, nameX + 5, nameY + 13);
    }

    /**
     * Draws all of the entity's boundaries
     *
     * @see Boundary#draw(Graphics)
     */
    private void drawBoundaries(Graphics graphics) {
        boundaries.forEach(boundary -> boundary.draw(graphics));
    }

    /**
     * "Kills" the entity by removing it from the entity store, but only if it is sector-specific
     */
    public void kill() {
        if (isSectorSpecific) {
            EntityStore.remove(this);
        }
    }

    /**
     * Should be called 60 times per second within a tick thread
     */
    public void tick() {
        boundaries.forEach(EntityBoundary::tick);
        collisions.forEach(EntityEntityCollision::tick);
    }

    /**
     * Changes the entity's location ({@link #x} and {@link #y}) at the specified speed, unless the specified boundary is outside of the window
     */
    protected void move(byte direction, EntityBoundary boundary) {
        isMovingUpwards = false;
        isMovingDownwards = false;
        isMovingLeftwards = false;
        isMovingRightwards = false;
        switch (direction) {
            case UP:
                if (boundary.top <= 0) {
                    break; // top of window
                }
                isMovingUpwards = true;
                y -= speed;
                onMove(UP);
                break;
            case DOWN:
                if (boundary.bottom >= Game.WINDOW_HEIGHT) {
                    break; // bottom of window
                }
                isMovingDownwards = true;
                y += speed;
                onMove(DOWN);
                break;
            case LEFT:
                if (boundary.left <= 0) {
                    break; // left side of window
                }
                isMovingLeftwards = true;
                x -= speed;
                onMove(LEFT);
                break;
            case RIGHT:
                if (boundary.right >= Game.WINDOW_WIDTH) {
                    break; // right side of window
                }
                isMovingRightwards = true;
                x += speed;
                onMove(RIGHT);
                break;
            default:
                break;
        }
    }

    protected void onMove(byte direction) {

    }

    /**
     * Changes the entity's location ({@link #x} and {@link #y}) at a rate of {@link #speed} per call. Since there is no boundary parameter, the entity's {@link #defaultBoundary} will be used
     */
    protected void move(byte direction) {
        move(direction, defaultBoundary);
    }

    /**
     * Have the entity "follow", or constantly move towards, the other
     *
     * @see #move(byte)
     */
    protected void follow(Entity e) {
        boolean left = isLeftOf(e);
        boolean below = isBelow(e);
        boolean above = isAbove(e);
        boolean right = isRightOf(e);

        boolean caughtUpRight = Calculate.getDifference(defaultBoundary.left, e.defaultBoundary.right) <= 8;
        boolean caughtUpLeft = Calculate.getDifference(defaultBoundary.right, e.defaultBoundary.left) <= 8;

        if (caughtUpLeft || caughtUpRight) {
            if (this instanceof AnimatedEntity) {
                AnimatedEntity ae = (AnimatedEntity) this;
                ae.animation.stop();
            }
        }

        // Debug.info("right diff: " + Calculate.getDifference(defaultBoundary.left, e.defaultBoundary.right) + ", left diff: " + Calculate.getDifference(defaultBoundary.right, e.defaultBoundary.left));
        // Debug.info("left: " + left + ", right: " + right);

        // horizontal
        if (Calculate.getDifference(defaultBoundary.left, e.defaultBoundary.left) >= e.defaultBoundary.width) {
            // prevent "shaking" left and right
            if (!caughtUpRight && right) {
                move(LEFT);
            } else if (!caughtUpLeft && left) {
                move(RIGHT);
            }
        }

        // vertical
        if (Calculate.getDifference(defaultBoundary.top, e.defaultBoundary.top) < 2)
            return; // prevent "shaking" up and down

        if (above) {
            move(DOWN, defaultBoundary);
        } else if (below) {
            move(UP, defaultBoundary);
        }
    }

    /**
     * Returns whether or not the entity is to the "right" of the other
     */
    private boolean isRightOf(Entity e) {
        return x > e.x;
    }

    /**
     * Returns whether or not the entity is to the "left" of the other
     */
    private boolean isLeftOf(Entity e) {
        return !isRightOf(e);
    }

    /**
     * Returns whether or not the entity is "above" the other
     */
    private boolean isAbove(Entity e) {
        return y < e.y;
    }

    /**
     * Returns whether or not the entity is "below" the other
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
     * @see #collisions
     */
    protected abstract void setCollisions();

    /**
     * Sets the current sprite
     *
     * @see #sprite
     */
    public void setSprite(int index) {
        sprite = sprites.get(index);
    }

    /**
     * Generates a new UUID
     *
     * @see #uuid
     */
    private void generateUUID() {
        uuid = Util.randomInt(9999, 1000, true);
    }

    /**
     * Returns the entity as a human-readable string, in the format "{@link #id}#{@link #uuid}"
     *
     * @see #id
     * @see #uuid
     */
    public String toString() {
        return id + "#" + uuid;
    }

}
