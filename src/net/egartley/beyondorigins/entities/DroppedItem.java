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

public class DroppedItem extends StaticEntity {

    public Item item;

    public DroppedItem(Item item, int x, int y) {
        super(item.id + "_dropped", new Sprite(Util.resize(item.image, item.image.getWidth() / 2, item.image.getHeight() / 2)));
        isSectorSpecific = true;
        isDualRendered = false;
        isTraversable = true;
        sprites.add(sprite);
        image = sprite.toBufferedImage();
        setPosition(x, y);

        this.item = item;
    }

    private boolean pickup() {
        if (!Game.in().inventory.isFull()) {
            Game.in().inventory.put(item);
            Game.in().map.sector.removeEntity(this);
            kill();
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
            public void onCollide(EntityEntityCollisionEvent event) {
                if (pickup()) {
                    end();
                }
            }
        });
    }
}
