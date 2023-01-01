package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.engine.map.Cutscene;
import net.egartley.beyondorigins.engine.entities.Entity;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.engine.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;

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
