package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.abstracts.StaticEntity;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;

/**
 * An area that will trigger a change in the player's location and floor within a building
 */
public class BuildingChanger extends StaticEntity {

    public int width;
    public int height;
    public int jumpNumber;
    public byte action;
    public static final byte UPSTAIRS = 0;
    public static final byte DOWNSTAIRS = 1;
    public static final byte LEAVE = 2;
    /**
     * Same as up or down, but doesn't mean the change is directly the floor above or below
     */
    public static final byte JUMP = 3;

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
        defaultBoundary = new EntityBoundary(this, width, height, new BoundaryPadding(0));
        boundaries.add(defaultBoundary);
    }

    @Override
    public void setCollisions() {
    }

    @Override
    protected void setInteractions() {
    }

}
