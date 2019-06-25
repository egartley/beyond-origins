package net.egartley.beyondorigins.entities;

import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.interfaces.Collidable;
import net.egartley.gamelib.interfaces.Interactable;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.logic.interaction.EntityEntityInteraction;
import net.egartley.gamelib.objects.StaticEntity;

/**
 * Basic tree that the player can walk under, but not over
 */
public class DefaultTree extends StaticEntity implements Collidable, Interactable {

    public DefaultTree(Sprite sprite, double x, double y) {
        super("Tree", sprite);
        this.x = x;
        this.y = y;
        setBoundaries();
        setCollisions();

        isSectorSpecific = true;
        isDualRendered = true;
        isTraversable = false;
        // set the first layer as the leaves
        firstLayer = image.getSubimage(0, 45, image.getWidth(), 19);
        // set the second layer as the trunk
        secondLayer = image.getSubimage(0, 0, image.getWidth(), 45);
    }

    @Override
    protected void setBoundaries() {
        defaultBoundary = new EntityBoundary(this, image.getWidth(), image.getHeight(),
                new BoundaryPadding(-24, -24, -24, -24));
        boundaries.add(defaultBoundary);
    }

    @Override
    public void setCollisions() {
    }

    @Override
    public void setInteractions() {

    }

    @Override
    public void hookInteraction(EntityEntityInteraction interaction) {

    }

    @Override
    public void tick() {
    }
    
}
