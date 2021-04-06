package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;

/**
 * An area that will trigger a change in the player's location and floor within a building
 */
public class BuildingChanger extends Entity {

    public int width;
    public int height;
    public byte action;
    public int jumpNumber;
    public static final byte JUMP = 3;
    public static final byte LEAVE = 2;
    public static final byte UPSTAIRS = 0;
    public static final byte DOWNSTAIRS = 1;

    public BuildingChanger(byte action, int x, int y, int width, int height) {
        super("BuildingChanger");
        this.width = width;
        this.height = height;
        this.action = action;
        setPosition(x, y);
        setBoundaries();
    }

    public BuildingChanger(byte action, int jumpNumber, int x, int y, int width, int height) {
        this(action, x, y, width, height);
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

    @Override
    protected void setInteractions() {
    }

}
