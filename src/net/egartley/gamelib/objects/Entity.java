package net.egartley.gamelib.objects;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.entities.EntityStore;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.logic.math.Calculate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Anything that is rendered with a {@link Sprite} at a specified location
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
     * Collection of the entity's boundaries
     */
    public ArrayList<EntityBoundary> boundaries;
    /**
     * The sprite to use while rendering
     */
    public Sprite sprite;
    /**
     * The buffered image to use when rendering (default)
     */
    protected BufferedImage image;
    /**
     * If {@link #isDualRendered} is true, render this before the player
     * ("below")
     */
    protected BufferedImage firstLayer;
    /**
     * If {@link #isDualRendered} is true, render this after the player
     * ("above")
     */
    protected BufferedImage secondLayer;
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
     * The entity's x-axis coordinate
     */
    public double x;
    /**
     * The entity's y-axis coordinate
     */
    public double y;
    /**
     * The entity's speed (magnitude of its location change when moving)
     */
    public double speed;
    /**
     * The entity's unique identification number. Use {@link #id} for
     * user-friendly identification
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
     * Whether or not other entities, mainly the player, can walk over it
     * or not
     */
    public boolean isTraversable;
    /**
     * Whether or not the entity is currently collided with another entity
     */
    public boolean isCollided;
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
    protected boolean isDualRendered;
    /**
     * Whether or not the entity is currently registered in the entity
     * store
     */
    public boolean isRegistered;
    /**
     * Whether or not the entity is "bound" to, or only exists in, a
     * specific map sector
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
     * Whether or not font metrics have been initialized. Since {@link
     * #render(Graphics)} is called about 60 times a second, and the
     * resulting font metrics object will always be the same, there is no
     * need to keep re-computing it each time {@link #render(Graphics)} is
     * called
     */
    private boolean setFontMetrics;

    /**
     * Creates a new entity with a randomly generated UUID, an initial
     * speed of <code>1.0</code>, then adds it to the entity store
     */
    Entity(String id) {
        generateUUID();
        this.id = id;
        speed = 1.0;
        boundaries = new ArrayList<>();
        EntityStore.register(this);
    }

    /**
     * Renders the entity, using {@link #sprite}, at ({@link #x}, {@link #y})
     */
    public void render(Graphics graphics) {
        graphics.drawImage(sprite.toBufferedImage(0), (int) x, (int) y, null);
        drawDebug(graphics);
    }

    /**
     * Draws the first "layer", assuming {@link #isDualRendered} is true (below the player)
     */
    public void drawFirstLayer(Graphics graphics) {
        graphics.drawImage(firstLayer, (int) x, (int) y + secondLayer.getHeight(), null);
    }

    /**
     * Draws the second "layer", assuming {@link #isDualRendered} is true (above the player)
     */
    public void drawSecondLayer(Graphics graphics) {
        graphics.drawImage(secondLayer, (int) x, (int) y, null);
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
    }

    /**
     * Draws the entity's "name tag", which is {@link #toString()} with a half-opaque black background
     */
    private void drawNameTag(Graphics graphics) {
        if (!setFontMetrics) {
            if (name == null || name.equals(""))
                name = toString();
            nameTagWidth = graphics.getFontMetrics(nameTagFont).stringWidth(name) + 8;
            entityWidth = sprite.width;
            setFontMetrics = true;
        }
        nameX = Calculate.getCenter((int) x, entityWidth) - nameTagWidth / 2;
        nameY = (int) y - 18;

        graphics.setColor(nameTagBackgroundColor);
        graphics.setFont(nameTagFont);

        graphics.fillRect(nameX, nameY, nameTagWidth, 18);
        graphics.setColor(Color.WHITE);
        graphics.drawString(name, nameX + 5, nameY + 13);
    }

    /**
     * Draws all of the entity's boundaries
     */
    private void drawBoundaries(Graphics graphics) {
        boundaries.forEach(boundary -> boundary.draw(graphics));
    }

    /**
     * "Kills" the entity by removing it from the entity store, but only if
     * it is sector-specific
     */
    public void kill() {
        if (isSectorSpecific) {
            EntityStore.remove(this);
        } else {
            Debug.warning("Tried to kill \"" + this + "\", but it is not sector-specific");
        }
    }

    /**
     * For now, just calls {@link EntityBoundary#tick()}
     *
     * @see #boundaries
     */
    public void tick() {
        boundaries.forEach(EntityBoundary::tick);
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
        if (direction == UP && !isAllowedToMoveUpwards)
            return;
        if (direction == DOWN && !isAllowedToMoveDownwards)
            return;
        if (direction == LEFT && !isAllowedToMoveLeftwards)
            return;
        if (direction == RIGHT && !isAllowedToMoveRightwards)
            return;
        if (direction == -1)
            return;
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
     * @see #UP
     * @see #DOWN
     * @see #LEFT
     * @see #RIGHT
     * @see #move(byte)
     */
    protected void onMove(byte direction) {
    }

    /**
     * Changes the entity's location ({@link #x} and {@link #y}) at a rate
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
        return x > e.x;
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
        return y < e.y;
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
     * Sets the current sprite
     *
     * @see #sprite
     */
    public void setSprite(int index) {
        if (index < sprites.size())
            sprite = sprites.get(index);
        else
            Debug.warning("Tried to get a sprite for \"" + this + "\" at an valid index, " + index + " (must be less than " + sprites.size() + ")");
    }

    /**
     * Generates a new value for {@link #uuid}
     *
     * @see Util#randomInt(int, int, boolean)
     */
    private void generateUUID() {
        int gen = Util.randomInt(9999, 1000, true);
        if (EntityStore.getEntityByUUID(gen) != null) {
            // there's already an entity with the same uuid, so generate another one
            generateUUID();
            return;
        }
        uuid = gen;
    }

    /**
     * Returns the entity as a string, in the format <code>id#uuid</code>
     *
     * @see #id
     * @see #uuid
     */
    public String toString() {
        return id + "#" + uuid;
    }

}
