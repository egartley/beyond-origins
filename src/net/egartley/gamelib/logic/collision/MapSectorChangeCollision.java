package net.egartley.gamelib.logic.collision;

import net.egartley.gamelib.abstracts.Map;
import net.egartley.gamelib.abstracts.MapSector;
import net.egartley.gamelib.logic.interaction.Boundary;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.logic.interaction.MapSectorChangeBoundary;

import java.awt.*;

public class MapSectorChangeCollision {

    private boolean isCollided;
    private boolean firedEvent;

    private Map map;
    private MapSector from;
    private MapSector to;
    private Boundary[] boundaries = new Boundary[2];
    private Rectangle[] rectangles = new Rectangle[2];

    public MapSectorChangeCollision(MapSectorChangeBoundary changeBoundary, EntityBoundary playerBoundary, MapSector
            to, MapSector from, Map map) {
        boundaries[0] = changeBoundary;
        boundaries[1] = playerBoundary;
        rectangles[0] = boundaries[0].asRectangle();
        rectangles[1] = boundaries[1].asRectangle();
        rectangles[0].width++;
        rectangles[0].height++;
        rectangles[1].width++;
        rectangles[1].height++;
        this.to = to;
        this.from = from;
        this.map = map;
    }

    public void tick() {
        rectangles[0].x = boundaries[0].x - 1;
        rectangles[1].x = boundaries[1].x - 1;
        rectangles[0].y = boundaries[0].y - 1;
        rectangles[1].y = boundaries[1].y - 1;
        isCollided = rectangles[0].intersects(rectangles[1]);

        if (isCollided && !firedEvent) {
            onCollide();
            firedEvent = true;
        }

        if (!isCollided && firedEvent)
            firedEvent = false;
    }

    /**
     * This is called <b>once</b> when the collision occurs
     */
    private void onCollide() {
        map.changeSector(to, from);
    }

}
