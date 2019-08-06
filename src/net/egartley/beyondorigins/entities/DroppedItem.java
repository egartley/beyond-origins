package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.ingame.Item;
import net.egartley.gamelib.objects.StaticEntity;

public class DroppedItem extends StaticEntity {

    public Item item;

    public DroppedItem(Item item) {
        super("dropped_" + item.name);
        isSectorSpecific = true;

        this.item = item;
    }

    @Override
    protected void setBoundaries() {

    }

}
