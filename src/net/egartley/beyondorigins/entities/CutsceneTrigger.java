package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.abstracts.Cutscene;
import net.egartley.beyondorigins.core.abstracts.StaticEntity;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;

/**
 * An area that will trigger a cutscene to start playing
 */
public class CutsceneTrigger extends StaticEntity {

    public Cutscene cutscene;

    public CutsceneTrigger(Cutscene cutscene, int x, int y) {
        super("Cutscene", Entities.getSpriteTemplate(Entities.TEMPLATE_CT));
        setPosition(x, y);
        isSectorSpecific = true;
        this.cutscene = cutscene;
    }

    @Override
    protected void setBoundaries() {
        defaultBoundary = new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(0));
        boundaries.add(defaultBoundary);
    }

    @Override
    public void setCollisions() {
        EntityEntityCollision collision = new EntityEntityCollision(defaultBoundary, Entities.PLAYER.boundary) {
            @Override
            public void start(EntityEntityCollisionEvent event) {
                cutscene.start();
            }
        };
        Collisions.add(collision);
    }

    @Override
    protected void setInteractions() {

    }

}
