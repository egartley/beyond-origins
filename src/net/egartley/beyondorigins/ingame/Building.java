package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.core.abstracts.StaticEntity;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.InGameState;

import java.util.ArrayList;

public class Building extends StaticEntity {

    public int playerLeaveX, playerLeaveY;
    public EntityBoundary entryBoundary;
    public BuildingFloor entryFloor;
    public BuildingFloor currentFloor;
    public EntityEntityCollision playerCollision;
    public ArrayList<BuildingFloor> floors = new ArrayList<>();

    public Building(String id, int x, int y, int playerLeaveX, int playerLeaveY) {
        super(id, new SpriteSheet(Images.get("resources/images/buildings/" + id + ".png"), 1, 1).sprites.get(0));
        isSectorSpecific = true;
        isTraversable = true;
        setPosition(x, y);
        this.playerLeaveX = playerLeaveX;
        this.playerLeaveY = playerLeaveY;
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
        // Entities.PLAYER.invalidateAllMovement();
    }

    public void onPlayerEnter() {
        InGameState.building = this;
        Entities.PLAYER.enteredBuilding();
        entryFloor.onPlayerEnter(null);
    }

    public void onPlayerLeave() {
        currentFloor.onPlayerLeave();
        Entities.PLAYER.leftBuilding(this);
    }

    public void leave() {
        onPlayerLeave();
    }

    /**
     * Change the floor to the one above it
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
     * Change the floor to the one below it
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
        defaultBoundary = new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(1));
        entryBoundary = defaultBoundary;
        boundaries.add(defaultBoundary);
    }

    @Override
    public void setCollisions() {
        playerCollision = new EntityEntityCollision(entryBoundary, Entities.PLAYER.boundary) {
            @Override
            public void start(EntityEntityCollisionEvent event) {
                onPlayerEnter();
                end();
            }
        };
        Collisions.add(playerCollision);
    }

    @Override
    protected void setInteractions() {

    }

}
