package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Debug;
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
 * An object or character that can rendered with a {@link Sprite} at a
 * specified location
 *
 * @see AnimatedEntity
 * @see StaticEntity
 */
public abstract class Entity {

    public static final byte UP = 1;
    public static final byte DOWN = 2;
    public static final byte LEFT = 3;
    public static final byte RIGHT = 4;

    public static final byte FOLLOW_PASSIVE = 0;
    public static final byte FOLLOW_AGGRESSIVE = 1;

    private static final byte CAUGHT_UP_LEFT = 0;
    private static final byte CAUGHT_UP_RIGHT = 1;
    private static final byte CAUGHT_UP_VERTICAL = 2;

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
    protected boolean isMovingUpwards = false;
    /**
     * Whether or not the entity is currently moving downwards
     */
    protected boolean isMovingDownwards = false;
    /**
     * Whether or not the entity is currently moving leftwards
     */
    protected boolean isMovingLeftwards = false;
    /**
     * Whether or not the entity is currently moving rightwards
     */
    protected boolean isMovingRightwards = false;
    /**
     * Whether or not the entity is allowed to move upwards
     */
    protected boolean isAllowedToMoveUpwards = true;
    /**
     * Whether or not the entity is allowed to move downwards
     */
    protected boolean isAllowedToMoveDownwards = true;
    /**
     * Whether or not the entity is allowed to move leftwards
     */
    protected boolean isAllowedToMoveLeftwards = true;
    /**
     * Whether or not the entity is allowed to move rightwards
     */
    protected boolean isAllowedToMoveRightwards = true;
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
    private byte lastCaughtUpDirection = -1;
    /**
     * Whether or not font metrics have been initialized. Since {@link
     * #render(Graphics)} is called about 60 times a second, and the
     * resulting font metrics object will always be the same, there is no
     * need to keep re-computing it each time {@link #render(Graphics)} is
     * called, only the first
     */
    private boolean setFontMetrics = false;
    /**
     * Whether or not the {@link #onCaughtUp(byte)} event has been fired
     */
    private boolean didFireCatchUpEvent = false;

    /**
     * Creates a new entity with a randomly generated UUID, an initial
     * speed of <code>1.0</code>, then adds it to the entity store
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
     * Renders the entity, using {@link #sprite}, at ({@link #x}, {@link
     * #y})
     */
    public void render(Graphics graphics) {
        graphics.drawImage(sprite.toBufferedImage(0), (int) x, (int) y,
                null);
        drawDebug(graphics);
    }

    /**
     * Draws the first "layer" if {@link #isDualRendered} is true (below
     * the player)
     */
    public void drawFirstLayer(Graphics graphics) {
        graphics.drawImage(firstLayer, (int) x,
                (int) y + secondLayer.getHeight(), null);
    }

    /**
     * Draws the second "layer" if {@link #isDualRendered} is true (above
     * the player)
     */
    public void drawSecondLayer(Graphics graphics) {
        graphics.drawImage(secondLayer, (int) x, (int) y, null);
        drawDebug(graphics);
    }

    /**
     * Renders debug information, such as the entity's boundaries and "name
     * tag"
     *
     * @see Game#debug
     */
    void drawDebug(Graphics graphics) {
        if (Game.debug) {
            drawBoundaries(graphics);
            drawNameTag(graphics);
        }
    }

    /**
     * Draws the entity's "name tag", which is {@link #toString()} with a
     * half-opaque black background
     */
    private void drawNameTag(Graphics graphics) {
        if (!setFontMetrics) {
            if (name == null || name.equals(""))
                name = toString();
            nameTagWidth =
                    graphics.getFontMetrics(nameTagFont).stringWidth(name) + 8; // 4px padding both sides
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
     *
     * @see Boundary#draw(Graphics)
     */
    private void drawBoundaries(Graphics graphics) {
        boundaries.forEach(boundary -> boundary.draw(graphics));
    }

    /**
     * "Kills" the entity by removing it from the entity store, but only if
     * it is sector-specific
     */
    public void kill() {
        if (isSectorSpecific)
            EntityStore.remove(this);
        else
            Debug.warning("Tried to kill \"" + this + "\", but it is not" +
                    " sector-specific");
    }

    /**
     * Calls {@link EntityBoundary#tick()} and {@link
     * EntityEntityCollision#tick()}
     *
     * @see #boundaries
     * @see #collisions
     */
    public void tick() {
        boundaries.forEach(EntityBoundary::tick);
        collisions.forEach(EntityEntityCollision::tick);
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
     * @param direction
     *         Which direction the entity moved in
     *
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
     * Have the entity "follow", or constantly move towards, the other
     * entity
     *
     * @see #move(byte)
     */
    protected void follow(Entity toFollow, byte mode,
                          int verticalBoundaryDifference) {
        // initial calc
        boolean leftOf = isLeftOf(toFollow);
        boolean below = isBelow(toFollow);
        boolean above = isAbove(toFollow);
        boolean rightOf = isRightOf(toFollow);
        boolean caughtUpRight =
                Calculate.getDifference(defaultBoundary.left,
                        toFollow.defaultBoundary.right) <= 8;
        boolean caughtUpLeft =
                Calculate.getDifference(defaultBoundary.right,
                        toFollow.defaultBoundary.left) <= 8;
        boolean caughtUpVertical =
                Calculate.getDifference(defaultBoundary.top,
                        toFollow.defaultBoundary.top) <= verticalBoundaryDifference;

        // "caught up" event firing
        if (!didFireCatchUpEvent && (caughtUpLeft || caughtUpRight || caughtUpVertical)) {
            if (caughtUpLeft) {
                lastCaughtUpDirection = CAUGHT_UP_LEFT;
            } else if (caughtUpRight) {
                lastCaughtUpDirection = CAUGHT_UP_RIGHT;
            } else {
                lastCaughtUpDirection = CAUGHT_UP_VERTICAL;
            }
            onCaughtUp(lastCaughtUpDirection);
            didFireCatchUpEvent = true;
        } else if (didFireCatchUpEvent) {
            boolean end = false;
            switch (lastCaughtUpDirection) {
                case CAUGHT_UP_LEFT:
                    if (!caughtUpLeft) {
                        end = true;
                    }
                    break;
                case CAUGHT_UP_RIGHT:
                    if (!caughtUpRight) {
                        end = true;
                    }
                    break;
                case CAUGHT_UP_VERTICAL:
                    if (!caughtUpVertical) {
                        end = true;
                    }
                    break;
                default:
                    // still caught up, do not trigger end event
                    break;
            }
            if (end) {
                onCaughtUpEnd(lastCaughtUpDirection);
                didFireCatchUpEvent = false;
            }
        }

        // determine what direction(s) to move in
        byte directionToMove = -1;
        // HORIZONTAL
        if (Calculate.getDifference(defaultBoundary.left,
                toFollow.defaultBoundary.left) >= toFollow.defaultBoundary.width) {
            if (!caughtUpRight && rightOf) {
                directionToMove = LEFT;
            } else if (!caughtUpLeft && leftOf) {
                directionToMove = RIGHT;
            }
        }
        if (mode == FOLLOW_AGGRESSIVE) {
            if ((!isAllowedToMoveDownwards || !isAllowedToMoveUpwards) && !caughtUpVertical) {
                if (rightOf) {
                    directionToMove = LEFT;
                    // Debug.info("left");
                } else {
                    directionToMove = RIGHT;
                    // Debug.info("right");
                }
            }
        }
        if (directionToMove != -1) {
            move(directionToMove);
        }
        // VERTICAL
        if (!caughtUpVertical) {
            if (above) {
                directionToMove = DOWN;
            } else if (below) {
                directionToMove = UP;
            }
        }
        if (mode == FOLLOW_AGGRESSIVE) {
            if (!isAllowedToMoveLeftwards || !isAllowedToMoveRightwards) {
                if (above) {
                    // Debug.info("down");
                    directionToMove = DOWN;
                } else {
                    // Debug.info("up");
                    directionToMove = UP;
                }
            }
        }
        if (directionToMove != LEFT && directionToMove != RIGHT && directionToMove != -1) {
            move(directionToMove);
        }
    }

    /**
     * Called <b>once</b> when the entity "catches up" with the one it is
     * following
     *
     * @param direction
     *         {@link #CAUGHT_UP_LEFT}, {@link #CAUGHT_UP_RIGHT} or {@link
     *         #CAUGHT_UP_VERTICAL}
     *
     * @see #follow(Entity, byte, int)
     */
    protected void onCaughtUp(byte direction) {

    }

    /**
     * Called <b>once</b> when the entity is no longer "caught up" with the
     * one it is following
     *
     * @param direction
     *         {@link #CAUGHT_UP_LEFT}, {@link #CAUGHT_UP_RIGHT} or {@link
     *         #CAUGHT_UP_VERTICAL}
     *
     * @see #follow(Entity, byte, int)
     */
    protected void onCaughtUpEnd(byte direction) {

    }

    /**
     * Disallows movement opposite of the side of the collision. For
     * example, if the player collided with a tree on its top side,
     * downward movement would no longer be allowed
     */
    protected void onCollisionWithNonTraversableEntity(EntityEntityCollisionEvent event) {
        lastCollisionEvent = event;
        switch (event.collidedSide) {
            case EntityEntityCollisionEvent.RIGHT_SIDE:
                // collided on the right, so disable leftwards movement
                isAllowedToMoveLeftwards = false;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                // collided on the left, so disable rightwards movement
                isAllowedToMoveRightwards = false;
                break;
            case EntityEntityCollisionEvent.TOP_SIDE:
                // collided at the top, so disable downwards movement
                isAllowedToMoveDownwards = false;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                // collided at the bottom, so disable upwards movement
                isAllowedToMoveUpwards = false;
                break;
            default:
                break;
        }
    }

    /**
     * Cancels any movement restrictions imposed by the provided {@link
     * net.egartley.beyondorigins.logic.events .EntityEntityCollisionEvent
     * EntityEntityCollisionEvent}
     */
    protected void annulCollisionEvent(EntityEntityCollisionEvent event) {
        // check for other movement restrictions
        for (EntityEntityCollision c : concurrentCollisions) {
            if (c.lastEvent.collidedSide == event.collidedSide && c.lastEvent.invoker != event.invoker) {
                // there is another collision that has the same movement
                // restriction, so don't annul it
                return;
            }
        }

        switch (event.collidedSide) {
            case EntityEntityCollisionEvent.TOP_SIDE:
                isAllowedToMoveDownwards = true;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                isAllowedToMoveUpwards = true;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                isAllowedToMoveRightwards = true;
                break;
            case EntityEntityCollisionEvent.RIGHT_SIDE:
                isAllowedToMoveLeftwards = true;
                break;
            default:
                break;
        }
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
        if (index < sprites.size())
            sprite = sprites.get(index);
        else
            Debug.warning("Tried to get a sprite for \"" + this + "\" at" +
                    " an valid index, " + index + " (must be less " +
                    "than" +
                    " " + sprites.size() + ")");
    }

    /**
     * Generates a new value for {@link #uuid}
     *
     * @see Util#randomInt(int, int, boolean)
     */
    private void generateUUID() {
        uuid = Util.randomInt(9999, 1000, true);
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
