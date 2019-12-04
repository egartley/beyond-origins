package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.ingame.Item;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.abstracts.StaticEntity;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.threads.DelayedEvent;

/**
 * An item that was dropped from the player's inventory
 */
public class DroppedItem extends StaticEntity {

    /**
     * How long it takes for a dropped item to be able to be picked up again after being dropped
     */
    private static final double PICKUP_DELAY = 2.25D;
    /**
     * How long it takes for a dropped item to "despawn" after being dropped and not picked up again
     */
    private static final double LIFETIME_DELAY = 120.0D;

    private DelayedEvent lifetimeDelay;

    /**
     * Whether or not the dropped item can be picked up again
     *
     * @see #PICKUP_DELAY
     */
    public boolean canPickup;
    /**
     * Whether or not the player is currently "over" the dropped item
     */
    public boolean over;
    /**
     * The item being represented
     */
    public Item item;

    public DroppedItem(Item item, int x, int y) {
        super("DroppedItem", new Sprite(Util.resize(item.image, 0.5)));
        isSectorSpecific = true;
        isDualRendered = false;
        isTraversable = true;
        image = sprite.toBufferedImage();
        setPosition(x, y);

        new DelayedEvent(PICKUP_DELAY) {
            @Override
            public void onFinish() {
                canPickup = true;
                if (pickup()) {
                    collisions.get(0).end();
                }
            }
        }.start();
        lifetimeDelay = new DelayedEvent(LIFETIME_DELAY) {
            @Override
            public void onFinish() {
                destroy();
            }
        };
        lifetimeDelay.start();

        this.item = item;
    }

    /**
     * Removes the dropped item from the current sector's entities, and then kills it
     *
     * @see Entity#kill()
     */
    private void destroy() {
        Game.in().map.sector.removeEntity(this);
        kill();
    }

    /**
     * Attempt to pickup the dropped item
     *
     * @return Whether or not the dropped item was successfully picked up by the player
     */
    private boolean pickup() {
        if (!Game.in().playerMenu.isFull() && canPickup && over) {
            Game.in().playerMenu.put(item);
            lifetimeDelay.cancel();
            destroy();
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(1)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setCollisions() {
        collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.boundary) {
            @Override
            public void start(EntityEntityCollisionEvent event) {
                over = true;
                if (pickup()) {
                    end();
                }
            }

            @Override
            public void end(EntityEntityCollisionEvent event) {
                over = false;
            }
        });
    }

    @Override
    protected void setInteractions() {

    }
}
