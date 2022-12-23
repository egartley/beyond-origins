package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.abstracts.Cutscene;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;

/**
 * An area that will trigger a cutscene to start playing
 */
public class CutsceneTrigger extends Entity {

    public Cutscene cutscene;

    public CutsceneTrigger(Cutscene cutscene, int x, int y) {
        super("Cutscene", 32, 32);
        this.cutscene = cutscene;
        isSectorSpecific = true;
        setPosition(x, y);
        setBoundaries();
        setCollisions();
    }

    @Override
    protected void setBoundaries() {
        defaultBoundary = new EntityBoundary(this);
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

}
