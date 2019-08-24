package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.ingame.Inventory;
import net.egartley.beyondorigins.ingame.Item;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.objects.StaticEntity;

public class DroppedItem extends StaticEntity {

    public Item item;

    public DroppedItem(Item item, int x, int y) {
        super("dropped_" + item.name, new Sprite(item.image));
        isSectorSpecific = true;
        isDualRendered = false;
        isTraversable = true;
        sprites.add(sprite);
        image = sprite.toBufferedImage();
        setPosition(x, y);

        this.item = item;
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
        DroppedItem me = this;
        collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.boundary) {
            public void onCollide(EntityEntityCollisionEvent event) {
                Inventory.put(item);
                Game.in().getCurrentMap().sector.removeEntity(me);
                me.kill();
                end();
            }
        });
    }
}
