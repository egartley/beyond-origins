package net.egartley.beyondorigins.ingame.buildings;

import net.egartley.beyondorigins.entities.BuildingChanger;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.beyondorigins.ingame.BuildingFloor;
import net.egartley.gamelib.logic.interaction.BoundaryOffset;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.logic.math.Calculate;

public class House1 extends Building {

    private EntityBoundary blockBoundary1, blockBoundary2;

    public House1(int x, int y, int playerLeaveX, int playerLeaveY) {
        super("house-1", x, y, playerLeaveX, playerLeaveY);
        isDualRendered = true;
        firstLayer = image.getSubimage(0, 64, image.getWidth(), 58);
        secondLayer = image.getSubimage(0, 0, image.getWidth(), 64);

        BuildingFloor floor = new BuildingFloor(0, this) {
            @Override
            public void onPlayerEnter(BuildingFloor from) {
                Entities.PLAYER.setPosition(Calculate.getCenteredX(Entities.PLAYER.sprite.width), 356);
            }
        };
        floor.upperYLimit = 152;
        floor.addChanger(new BuildingChanger(BuildingChanger.UPSTAIRS, 208, 311, 55, 49));
        floor.addChanger(new BuildingChanger(BuildingChanger.LEAVE, 447, 406, 58, 12));
        addFloor(floor);

        floor = new BuildingFloor(1, this) {
            @Override
            public void onPlayerEnter(BuildingFloor from) {
                Entities.PLAYER.setPosition(273, 316);
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
        defaultBoundary = new EntityBoundary(this, 31, 24, new BoundaryPadding(0), new BoundaryOffset(0, 75, 0, 53));
        blockBoundary1 = new EntityBoundary(this, 31, 23, new BoundaryPadding(0), new BoundaryOffset(0, 64, 0, 22));
        blockBoundary2 = new EntityBoundary(this, 30, 23, new BoundaryPadding(0), new BoundaryOffset(0, 64, 0, 84));

        entryBoundary = defaultBoundary;
        boundaries.add(defaultBoundary);
        boundaries.add(blockBoundary1);
        boundaries.add(blockBoundary2);
    }

    @Override
    protected void setCollisions() {
        super.setCollisions();
        Entities.PLAYER.generateMovementRestrictionCollisions(blockBoundary1, Entities.PLAYER.boundary);
        Entities.PLAYER.generateMovementRestrictionCollisions(blockBoundary2, Entities.PLAYER.boundary);
    }

}
