package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.abstracts.StaticEntity;
import net.egartley.gamelib.graphics.SpriteSheet;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;

import java.util.ArrayList;

public class Building extends StaticEntity {

    public EntityBoundary entryBoundary;
    public BuildingFloor entryFloor;
    public BuildingFloor currentFloor;
    public EntityEntityCollision playerCollision;
    public ArrayList<BuildingFloor> floors;

    public Building(String id) {
        super(id, new SpriteSheet(ImageStore.get("resources/images/buildings/" + id + ".png"), 1, 1).sprites.get(0));
        isSectorSpecific = true;
        isTraversable = true;

        this.floors = new ArrayList<>();
    }

    public void addFloor(BuildingFloor floor) {
        floors.add(floor);
        if (floors.size() == 1) {
            entryFloor = floor;
            currentFloor = entryFloor;
        }
    }

    public void addChanger() {
    }

    public void changeFloor(BuildingFloor floor) {
        currentFloor.onPlayerLeave();
        currentFloor = floor;
        currentFloor.onPlayerEnter();
    }

    public void changeFloor(int number) {
        // TODO: check number
        changeFloor(floors.get(number));
    }

    public void onPlayerEnter() {
        Entities.PLAYER.enteredBuilding();
        entryFloor.onPlayerEnter();
    }

    public void onPlayerLeave() {
        currentFloor.onPlayerLeave();
        Entities.PLAYER.leftBuilding();

        Game.in().setSubState(null);
    }

    public void leave() {
    }

    public void upstairs() {
        Debug.out("going up");
        BuildingFloor up = null;
        try {
            up = floors.get(floors.indexOf(currentFloor) + 1);
        } catch (IndexOutOfBoundsException e) {
            Debug.warning("Tried to go up a floor in \"" + id + "\", but already at the top floor!");
            return;
        }
        changeFloor(up);
    }

    public void downstairs() {
        BuildingFloor down = null;
        try {
            down = floors.get(floors.indexOf(currentFloor) - 1);
        } catch (IndexOutOfBoundsException e) {
            Debug.warning("Tried to go down a floor in \"" + id + "\", but already at the bottom floor!");
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
    protected void setCollisions() {
        Building me = this;
        playerCollision = new EntityEntityCollision(entryBoundary, Entities.PLAYER.boundary) {
            @Override
            public void onCollide(EntityEntityCollisionEvent event) {
                Game.in().setBuilding(me);
                Game.in().setSubState(Game.in().subStates.get(0));
                end();
            }
        };
        collisions.add(playerCollision);
    }

}
