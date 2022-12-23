package net.egartley.beyondorigins.ingame.buildings;

import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryOffset;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.entities.BuildingChanger;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Building;
import net.egartley.beyondorigins.ingame.BuildingFloor;

/**
 * Test building
 */
public class House1 extends Building {

    private EntityBoundary blockBoundary1;
    private EntityBoundary blockBoundary2;
    private EntityBoundary blockBoundary3;

    public House1(int x, int y, int playerLeaveX, int playerLeaveY) {
        super("House-1", x, y, playerLeaveX, playerLeaveY);
        isDualRendered = true;
        firstLayer = image.getSubImage(0, 64, image.getWidth(), 58);
        secondLayer = image.getSubImage(0, 0, image.getWidth(), 64);
        // first floor
        BuildingFloor floor = new BuildingFloor(0, this) {
            @Override
            public void onPlayerEnter(BuildingFloor from) {
                super.onPlayerEnter(from);
                if (from != null && from.equals(this.parent.floors.get(1))) {
                    // came down from the second floor
                    Entities.PLAYER.setPosition(220, 229);
                } else {
                    // presumably just entered
                    Entities.PLAYER.setPosition(Calculate.getCenteredX(Entities.PLAYER.sprite.width), 356);
                }
            }
        };
        floor.upperYLimit = 152;
        floor.addChanger(new BuildingChanger(BuildingChanger.UPSTAIRS, 208, 311, 55, 49));
        floor.addChanger(new BuildingChanger(BuildingChanger.LEAVE, 447, 406, 58, 12));
        addFloor(floor);
        // second floor
        floor = new BuildingFloor(1, this) {
            @Override
            public void onPlayerEnter(BuildingFloor from) {
                super.onPlayerEnter(from);
                Entities.PLAYER.setPosition(223, 282);
                Entities.WIZARD.setPosition(658, 216);
            }
        };
        floor.upperYLimit = 152;
        floor.addChanger(new BuildingChanger(BuildingChanger.DOWNSTAIRS, 208, 211, 55, 49));
        floor.addEntity(Entities.WIZARD);
        addFloor(floor);
    }

    @Override
    protected void setBoundaries() {
        defaultBoundary = new EntityBoundary(this, 31, 24, new BoundaryPadding(0),
                new BoundaryOffset(0, 75, 0, 53));
        blockBoundary1 = new EntityBoundary(this, 31, 23, new BoundaryPadding(0),
                new BoundaryOffset(0, 64, 0, 22));
        blockBoundary2 = new EntityBoundary(this, 30, 23, new BoundaryPadding(0),
                new BoundaryOffset(0, 64, 0, 84));
        blockBoundary3 = new EntityBoundary(this, 31, 11, new BoundaryPadding(0),
                new BoundaryOffset(0, 64, 0, 53));
        entryBoundary = defaultBoundary;
        boundaries.add(defaultBoundary);
        boundaries.add(blockBoundary1);
        boundaries.add(blockBoundary2);
        boundaries.add(blockBoundary3);
    }

    @Override
    public void setCollisions() {
        Collisions.add(new EntityEntityCollision(entryBoundary, Entities.PLAYER.boundary) {
            @Override
            public void start(EntityEntityCollisionEvent event) {
                onPlayerEnter();
                end();
            }
        });
        Entities.PLAYER.generateMovementRestrictionCollisions(blockBoundary1, blockBoundary2, blockBoundary3);
    }

}
