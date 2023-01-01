package net.egartley.beyondorigins.engine.logic.collision;

import net.egartley.beyondorigins.engine.map.Map;
import net.egartley.beyondorigins.engine.map.MapSector;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.Boundary;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.MapSectorChangeBoundary;

import java.awt.*;

/**
 * A collisions that occurs when the player goes from one sector to another
 */
public class MapSectorChangeCollision {

    private boolean didFireEvent;
    private final Map map;
    private final MapSector goingTo;
    private final MapSector comingFrom;
    private final Boundary[] boundaries = new Boundary[2];
    private final Rectangle[] rectangles = new Rectangle[2];

    public MapSectorChangeCollision(MapSectorChangeBoundary changeBoundary, EntityBoundary playerBoundary, MapSector goingTo, MapSector comingFrom, Map map) {
        boundaries[0] = changeBoundary;
        boundaries[1] = playerBoundary;
        rectangles[0] = boundaries[0].asRectangle();
        rectangles[1] = boundaries[1].asRectangle();
        rectangles[0].width++;
        rectangles[0].height++;
        rectangles[1].width++;
        rectangles[1].height++;
        this.goingTo = goingTo;
        this.comingFrom = comingFrom;
        this.map = map;
    }

    public void tick() {
        rectangles[0].x = boundaries[0].x - 1;
        rectangles[1].x = boundaries[1].x - 1;
        rectangles[0].y = boundaries[0].y - 1;
        rectangles[1].y = boundaries[1].y - 1;
        boolean isCollided = rectangles[0].intersects(rectangles[1]);
        if (isCollided && !didFireEvent) {
            onCollision();
            didFireEvent = true;
        }
        if (!isCollided && didFireEvent) {
            didFireEvent = false;
        }
    }

    private void onCollision() {
        map.changeSector(goingTo, comingFrom);
    }

}
