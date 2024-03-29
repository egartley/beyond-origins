package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.engine.entities.Entity;
import net.egartley.beyondorigins.engine.entities.VisibleEntity;
import net.egartley.beyondorigins.engine.graphics.Sprite;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.engine.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.BoundaryPadding;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.engine.logic.inventory.ItemStack;
import net.egartley.beyondorigins.engine.threads.DelayedEvent;
import net.egartley.beyondorigins.gamestates.InGameState;

/**
 * An item that was dropped from the player's inventory (by the user dragging it out, or programatically)
 */
public class DroppedItem extends VisibleEntity {

    private final DelayedEvent lifetimeDelay;
    private static final double PICKUP_DELAY = 2.25D;
    private static final double LIFETIME_DELAY = 120.0D;

    public boolean canPickup;
    public boolean isPlayerOver;
    public ItemStack itemStack;

    public DroppedItem(ItemStack stack, int x, int y) {
        super("DroppedItem", new Sprite(stack.item.image.getScaledCopy(0.5F)));
        itemStack = stack;
        isSectorSpecific = true;
        isTraversable = true;
        image = sprite.asImage();
        setPosition(x, y);
        Entity me = this;
        new DelayedEvent(PICKUP_DELAY) {
            @Override
            public void onFinish() {
                canPickup = true;
                if (pickup()) {
                    Collisions.endAllWith(me);
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
    }

    /**
     * Removes the dropped item from the current sector's entities, and then kills it
     */
    private void destroy() {
        InGameState.map.sector.removeEntity(this);
    }

    /**
     * Attempt to pick up the dropped item
     *
     * @return Whether the dropped item was successfully picked up by the player
     */
    private boolean pickup() {
        if (!Entities.PLAYER.inventory.isFull() && canPickup && isPlayerOver) {
            Entities.PLAYER.inventory.putStack(itemStack);
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
        boundaries.add(new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(1)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setCollisions() {
        Collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.boundary) {
            @Override
            public void start(EntityEntityCollisionEvent event) {
                isPlayerOver = true;
                if (pickup()) {
                    end();
                }
            }

            @Override
            public void end(EntityEntityCollisionEvent event) {
                isPlayerOver = false;
            }
        });
    }

}
