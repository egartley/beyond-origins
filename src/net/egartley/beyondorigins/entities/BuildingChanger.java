package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.engine.entities.Entity;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;

/**
 * An area that will trigger a change in the player's location and floor within a building
 */
public class BuildingChanger extends Entity {

    public byte actionType;
    public int jumpNumber;
    public static final byte JUMP = 3;
    public static final byte LEAVE = 2;
    public static final byte UPSTAIRS = 0;
    public static final byte DOWNSTAIRS = 1;

    public BuildingChanger(byte actionType, int x, int y, int width, int height) {
        super("BuildingChanger", width, height);
        this.actionType = actionType;
        setPosition(x, y);
        setBoundaries();
    }

    public BuildingChanger(byte actionType, int jumpNumber, int x, int y, int width, int height) {
        this(actionType, x, y, width, height);
        this.jumpNumber = jumpNumber;
    }

    @Override
    protected void setBoundaries() {
        defaultBoundary = new EntityBoundary(this, width, height);
        boundaries.add(defaultBoundary);
    }

    @Override
    public void setCollisions() {

    }

}
