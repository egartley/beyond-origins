package net.egartley.beyondorigins.ingame.buildings;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.beyondorigins.ingame.BuildingFloor;

public class House1 extends Building {

    public House1() {
        super("house-1", 1);
        setFloor(new BuildingFloor(0, this) {
            @Override
            public void onPlayerEnter() {
                Debug.out("You made it!");
            }
        });
    }

}
