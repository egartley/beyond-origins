package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Player;
import net.egartley.beyondorigins.gamestates.InGameState;

import java.util.ArrayList;

/**
 * A place that the player can enter, treated seperately from the current map
 */
public class Building extends Entity {

    public int playerLeaveX;
    public int playerLeaveY;
    public BuildingFloor entryFloor;
    public BuildingFloor currentFloor;
    public EntityBoundary entryBoundary;
    public ArrayList<BuildingFloor> floors = new ArrayList<>();

    public Building(String id, int x, int y, int playerLeaveX, int playerLeaveY) {
        super(id, new SpriteSheet(Images.get("resources/images/buildings/" + id + ".png"), 1, 1).sprites.get(0));
        isSectorSpecific = true;
        // set isTraversable to true, since collisions are generated later
        isTraversable = true;
        setPosition(x, y);
        this.playerLeaveX = playerLeaveX;
        this.playerLeaveY = playerLeaveY;
    }

    /**
     * Add a floor to the building. Sets {@link #entryFloor} and {@link #currentFloor} if this is the first one to be
     * added
     *
     * @param floor The floor to add
     */
    public void addFloor(BuildingFloor floor) {
        floors.add(floor);
        if (floors.size() == 1) {
            entryFloor = floor;
            currentFloor = entryFloor;
        }
    }

    /**
     * Move the player from the current floor to another, setting {@link #currentFloor} to that new one
     *
     * @param floor The floor to move to
     * @see BuildingFloor#onPlayerLeave()
     * @see BuildingFloor#onPlayerEnter(BuildingFloor)
     */
    public void changeFloor(BuildingFloor floor) {
        currentFloor.onPlayerLeave();
        floor.onPlayerEnter(currentFloor);
        currentFloor = floor;
        // require the user to re-press WASD keys after changing floors??
        // Entities.PLAYER.invalidateAllMovement();
    }

    /**
     * Called when the player enters the building
     *
     * @see #entryFloor
     * @see #entryBoundary
     * @see Player#enteredBuilding()
     */
    public void onPlayerEnter() {
        InGameState.building = this;
        Collisions.nuke();
        Entities.PLAYER.enteredBuilding();
        entryFloor.onPlayerEnter(null);
    }

    /**
     * Called when the player leaves the building
     *
     * @see #playerLeaveX
     * @see #playerLeaveY
     * @see Player#leftBuilding(Building)
     */
    public void onPlayerLeave() {
        currentFloor.onPlayerLeave();
        Entities.PLAYER.leftBuilding(this);
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

    @Override
    protected void setBoundaries() {
        // this may not be used or could be modified in inherents, but to avoid possible errors, make a generic boundary
        defaultBoundary = new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(1));
        entryBoundary = defaultBoundary;
        boundaries.add(defaultBoundary);
    }

    @Override
    public void setCollisions() {
        // rely on inherents to implement their own collisions
    }

    @Override
    protected void setInteractions() {

    }

}
