package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.ingame.Item;
import net.egartley.gamelib.abstracts.StaticEntity;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.threads.DelayedEvent;

public class DroppedItem extends StaticEntity {

    private static final double PICKUP_DELAY = 2.25D;
    private static final double LIFETIME_DELAY = 120.0D;

    private DelayedEvent lifetimeDelay;

    public boolean canPickup;
    public boolean over;
    public Item item;

    public DroppedItem(Item item, int x, int y) {
        super("DroppedItem_" + item.id, new Sprite(Util.resize(item.image, 0.5)));
        isSectorSpecific = true;
        isDualRendered = false;
        isTraversable = true;
        image = sprite.toBufferedImage();
        setPosition(x, y);

        new DelayedEvent(PICKUP_DELAY) {
            @Override
            public void onFinish() {
                // 2 and 1/4 seconds
                canPickup = true;
                if (pickup()) {
                    collisions.get(0).end();
                }
            }
        }.start();
        lifetimeDelay = new DelayedEvent(LIFETIME_DELAY) {
            @Override
            public void onFinish() {
                // 2 minutes
                destroy();
            }
        };
        lifetimeDelay.start();

        this.item = item;
    }

    private void destroy() {
        Game.in().map.sector.removeEntity(this);
        kill();
    }

    private boolean pickup() {
        if (!Game.in().inventory.isFull() && canPickup && over) {
            Game.in().inventory.put(item);
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
            public void onCollide(EntityEntityCollisionEvent event) {
                over = true;
                if (pickup()) {
                    end();
                }
            }
            @Override
            public void onCollisionEnd(EntityEntityCollisionEvent event) {
                over = false;
            }
        });
    }
}
