package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.ingame.Item;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.objects.StaticEntity;

public class DroppedItem extends StaticEntity {

    public Item item;

    public DroppedItem(Item item, int x, int y) {
        super("dropped_" + item.name);
        isSectorSpecific = true;
        isDualRendered = false;
        sprite = new Sprite(item.image);
        sprites.add(sprite);
        image = sprite.toBufferedImage();
        setPosition(x, y);

        this.item = item;
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(1)));
        defaultBoundary = boundaries.get(0);
    }

}
