package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.engine.entities.VisibleEntity;
import net.egartley.beyondorigins.engine.graphics.SpriteSheet;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.BoundaryPadding;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.ingame.BuildingFloor;

import java.util.ArrayList;

/**
 * A place that the player can enter, treated seperately from the current map
 */
public class Building extends VisibleEntity {

    public int playerLeaveX;
    public int playerLeaveY;
    public BuildingFloor entryFloor;
    public BuildingFloor currentFloor;
    public EntityBoundary entryBoundary;
    public ArrayList<BuildingFloor> floors = new ArrayList<>();

    public Building(String id, int x, int y, int playerLeaveX, int playerLeaveY) {
        super(id, new SpriteSheet(Images.getImageFromPath("resources/images/buildings/" + id + ".png"),
                1, 1).sprites.get(0));
        isSectorSpecific = true;
        // set isTraversable to true, even though it's not, since collisions are generated later
        isTraversable = true;
        setPosition(x, y);
        this.playerLeaveX = playerLeaveX;
        this.playerLeaveY = playerLeaveY;
    }

    /**
     * Change the floor to the one above it (warning if already at top floor)
     */
    public void upstairs() {
        BuildingFloor up;
        try {
            up = floors.get(floors.indexOf(currentFloor) + 1);
        } catch (IndexOutOfBoundsException e) {
            Debug.warning("Tried to go up a floor in \"" + name + "\", but already at the top floor!");
            return;
        }
        changeFloor(up);
    }

    /**
     * Change the floor to the one below it (warning if already at bottom floor)
     */
    public void downstairs() {
        BuildingFloor down;
        try {
            down = floors.get(floors.indexOf(currentFloor) - 1);
        } catch (IndexOutOfBoundsException e) {
            Debug.warning("Tried to go down a floor in \"" + name + "\", but already at the bottom floor!");
            return;
        }
        changeFloor(down);
    }

    public void onPlayerEnter() {
        InGameState.building = this;
        Collisions.endAndNuke();
        Entities.PLAYER.enteredBuilding();
        entryFloor.onPlayerEnter(null);
    }

    public void onPlayerLeave() {
        currentFloor.onPlayerLeave();
        Entities.PLAYER.leftBuilding(this);
    }

    public void addFloor(BuildingFloor floor) {
        floors.add(floor);
        if (floors.size() == 1) {
            entryFloor = floor;
            currentFloor = entryFloor;
        }
    }

    public void changeFloor(BuildingFloor floor) {
        currentFloor.onPlayerLeave();
        floor.onPlayerEnter(currentFloor);
        currentFloor = floor;
    }

    @Override
    protected void setBoundaries() {
        defaultBoundary = new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(1));
        entryBoundary = defaultBoundary;
        boundaries.add(defaultBoundary);
    }

    @Override
    public void setCollisions() {

    }

}
