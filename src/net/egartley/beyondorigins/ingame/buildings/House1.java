package net.egartley.beyondorigins.ingame.buildings;

import net.egartley.beyondorigins.entities.BuildingChanger;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.beyondorigins.ingame.BuildingFloor;
import net.egartley.gamelib.logic.math.Calculate;

public class House1 extends Building {

    public House1() {
        super("house-1");

        BuildingFloor floor = new BuildingFloor(0, this) {
            @Override
            public void onPlayerEnter() {
                Entities.PLAYER.setPosition(Calculate.getCenteredX(Entities.PLAYER.sprite.width), 356);
            }
        };
        floor.upperYLimit = 152;
        floor.addChanger(new BuildingChanger(BuildingChanger.UPSTAIRS, 300, 300, 50, 50));
        addFloor(floor);

        floor = new BuildingFloor(1, this) {
            @Override
            public void onPlayerEnter() {
                Entities.PLAYER.setPosition(Calculate.getCenteredX(Entities.PLAYER.sprite.width), 356);
            }
        };
        floor.upperYLimit = 152;
        floor.addChanger(new BuildingChanger(BuildingChanger.DOWNSTAIRS, 300, 300, 50, 50));
        addFloor(floor);
    }

}
