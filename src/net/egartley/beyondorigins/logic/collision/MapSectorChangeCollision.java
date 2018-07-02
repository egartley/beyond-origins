package net.egartley.beyondorigins.logic.collision;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.logic.interaction.Boundary;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeBoundary;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;

import java.awt.*;

public class MapSectorChangeCollision {

    private boolean isCollided;
    private boolean firedEvent;

    public Boundary boundary1;
    public Boundary boundary2;

    private Map map;
    private MapSector from;
    private MapSector to;
    private Rectangle rectangle1;
    private Rectangle rectangle2;

    public MapSectorChangeCollision(MapSectorChangeBoundary changeBoundary, EntityBoundary playerBoundary, MapSector to, MapSector from, Map map) {
        boundary1 = changeBoundary;
        boundary2 = playerBoundary;
        rectangle1 = boundary1.asRectangle();
        rectangle2 = boundary2.asRectangle();
        rectangle1.width++;
        rectangle1.height++;
        rectangle2.width++;
        rectangle2.height++;
        this.to = to;
        this.from = from;
        this.map = map;
    }

    public void tick() {
        rectangle1.x = boundary1.x - 1;
        rectangle2.x = boundary2.x - 1;
        rectangle1.y = boundary1.y - 1;
        rectangle2.y = boundary2.y - 1;
        isCollided = rectangle1.intersects(rectangle2);

        if (isCollided && !firedEvent) {
            onCollide();
            firedEvent = true;
        }
        if (!isCollided && firedEvent) {
            firedEvent = false;
        }
    }

    /**
     * This is called <b>once</b> when the collision occurs
     */
    private void onCollide() {
        map.changeSector(to, from);
    }

}
