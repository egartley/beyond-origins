package net.egartley.beyondorigins.ingame.buildings;

import net.egartley.beyondorigins.entities.BuildingChanger;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.beyondorigins.ingame.BuildingFloor;
import net.egartley.gamelib.logic.math.Calculate;

public class House1 extends Building {

    public House1(int x, int y, int playerLeaveX, int playerLeaveY) {
        super("house-1", x, y, playerLeaveX, playerLeaveY);

        BuildingFloor floor = new BuildingFloor(0, this) {
            @Override
            public void onPlayerEnter(BuildingFloor from) {
                Entities.PLAYER.setPosition(Calculate.getCenteredX(Entities.PLAYER.sprite.width), 356);
            }
        };
        floor.upperYLimit = 152;
        floor.addChanger(new BuildingChanger(BuildingChanger.UPSTAIRS, 313, 253, 55, 49));
        floor.addChanger(new BuildingChanger(BuildingChanger.LEAVE, 447, 406, 58, 12));
        addFloor(floor);

        floor = new BuildingFloor(1, this) {
            @Override
            public void onPlayerEnter(BuildingFloor from) {
                Entities.PLAYER.setPosition(377, 259);
                Entities.WIZARD.setPosition(658, 216);
            }
        };
        floor.upperYLimit = 152;
        floor.addChanger(new BuildingChanger(BuildingChanger.DOWNSTAIRS, 208, 231, 55, 49));
        floor.addEntity(Entities.WIZARD);
        addFloor(floor);
    }

}
