package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.core.abstracts.StaticEntity;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;

/**
 * An area that will trigger a change in the player's location/floor within a building
 */
public class BuildingChanger extends StaticEntity {

    public static final byte UPSTAIRS = 0, DOWNSTAIRS = 1, LEAVE = 2, JUMP = 3;

    public int width;
    public int height;
    public int jumpNumber;
    public byte action;

    public BuildingChanger(byte action, int x, int y, int width, int height) {
        super("BuildingChanger");
        setPosition(x, y);
        this.width = width;
        this.height = height;
        this.action = action;

        defaultBoundary = new EntityBoundary(this, width, height, new BoundaryPadding(0));
    }

    public BuildingChanger(byte action, int jumpNumber, int x, int y, int width, int height) {
        this(action, x, y, width, height);
        this.jumpNumber = jumpNumber;
    }

    @Override
    protected void setBoundaries() {
    }

    @Override
    protected void setCollisions() {
    }

    @Override
    protected void setInteractions() {
    }

}
