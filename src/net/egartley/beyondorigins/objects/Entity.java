package net.egartley.beyondorigins.objects;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.entities.EntityStore;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.logic.math.Calculate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * An object or character that can rendered with a sprite and have a specific
 * position
 *
 * @see AnimatedEntity
 * @see StaticEntity
 */
public abstract class Entity {

    /**
     * Collection of this entity's sprites
     */
    protected ArrayList<Sprite> sprites;
    /**
     * Collection of this entity's collisions
     */
    protected ArrayList<EntityEntityCollision> collisions;
    /**
     * Collection of this entity's boundaries
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
     * The most recent collision that has occurred for this entity. If no collisions
     * have occurred within this entity's lifetime, this will be null
     */
    public EntityEntityCollision lastCollision = null;
    /**
     * The most recent collision event to have occurred. This will be null if no
     * collision event has yet to take place
     */
    public EntityEntityCollisionEvent lastCollisionEvent = null;
    /**
     * The entity's x-axis coordinate (absolute)
     */
    public double x;
    /**
     * The entity's y-axis coordinate (absolute)
     */
    public double y;
    /**
     * The entity's unique identification number. Use {@link #id} for user-friendly
     * identification
     */
    public int uuid;
    /**
     * Whether or not this entity is animated
     */
    boolean isAnimated;
    /**
     * Whether or not this entity is static (no animation)
     */
    public boolean isStatic;
    /**
     * Whether ot not this entity is currently collided with another entity
     */
    public boolean isCollided;
    /**
     * Whether or not this entity has two different "layers" that are rendered
     * before and after the player
     */
    protected boolean isDualRendered;
    /**
     * Whether or not this entity is currently registered in the entity store
     */
    public boolean isRegistered;
    /**
     * Whether or not this entity is "bound" to, or only exists in, a specific map
     * sector
     */
    protected boolean isSectorSpecific;
    /**
     * Human-readable identifier for this entity
     */
    private String id;

    private String name;
    private Font nameTagFont = new Font("Arial", Font.PLAIN, 11);
    private Color nameTagBackgroundColor = new Color(0, 0, 0, 128);
    private boolean setFontMetrics = false;

    private int nameTagWidth;
    private int entityWidth;
    private int nameX;
    private int nameY;

    /**
     * Creates a new entity with a randomly generated UUID, then adds it to the
     * entity store
     *
     * @param id Human-readable ID for the entity
     */
    Entity(String id) {
        generateUUID();
        this.id = id;
        boundaries = new ArrayList<>();
        collisions = new ArrayList<>();
        EntityStore.register(this);
    }

    /**
     * Renders the entity
     *
     * @param graphics Graphics object to use
     */
    public void render(Graphics graphics) {
        graphics.drawImage(sprite.getCurrentFrameAsBufferedImage(), (int) x, (int) y, null);
        drawDebug(graphics);
    }

    /**
     * Draws the first "layer" if {@link #isDualRendered} is true (below the player)
     *
     * @param graphics Graphics object to use
     */
    public void drawFirstLayer(Graphics graphics) {
        graphics.drawImage(firstLayer, (int) x, (int) y + secondLayer.getHeight(), null);
    }

    /**
     * Draws the second "layer" if {@link #isDualRendered} is true (above the
     * player)
     *
     * @param graphics Graphics object to use
     */
    public void drawSecondLayer(Graphics graphics) {
        graphics.drawImage(secondLayer, (int) x, (int) y, null);
        drawDebug(graphics);
    }

    /**
     * Renders debug information, such as the entity's boundaries and "name tag"
     *
     * @param graphics Graphics object to use
     */
    protected void drawDebug(Graphics graphics) {
        if (Game.debug) {
            drawBoundaries(graphics);
            drawNameTag(graphics);
        }
    }

    /**
     * Draws the entity's "name tag", which displays its {@link #id} and
     * {@link #uuid}
     *
     * @param graphics Graphics object to use
     */
    private void drawNameTag(Graphics graphics) {
        if (!setFontMetrics) {
            // init, only run once
            name = toString();
            nameTagWidth = graphics.getFontMetrics(nameTagFont).stringWidth(name) + 8; // 4px padding on both sides
            entityWidth = sprite.frameWidth;
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
     * @param graphics Graphics object to use
     */
    private void drawBoundaries(Graphics graphics) {
        for (EntityBoundary boundary : boundaries) {
            boundary.draw(graphics);
        }
    }

    /**
     * "Kills" this entity by removing it from the entity store. Only for
     * sector-specific entities
     */
    public void kill() {
        if (isSectorSpecific) {
            EntityStore.remove(this);
        }
    }

    /**
     * Should be called 60 times per second within a tick thread
     */
    public abstract void tick();

    /**
     * Sets this entity's boundaries
     */
    protected abstract void setBoundaries();

    /**
     * Sets this entity's collisions
     */
    protected abstract void setCollisions();

    /**
     * Sets the current sprite
     *
     * @param index Position of a sprite within the sprite collection
     *              ({@link #sprites}) to set as the current one
     */
    public void setCurrentSprite(int index) {
        sprite = sprites.get(index);
    }

    /**
     * Generates a new UUID
     */
    private void generateUUID() {
        uuid = Util.randomInt(9999, 1000, true);
    }

    /**
     * Returns this entity as a human-readable string, in the format
     * "{@link #id}#{@link #uuid}"
     */
    public String toString() {
        return id + "#" + uuid;
    }

}
