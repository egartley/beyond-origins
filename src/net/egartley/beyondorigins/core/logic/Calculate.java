package net.egartley.beyondorigins.core.logic;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.enums.Direction;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;

public class Calculate {

    /**
     * Returns the "center" of b from a
     *
     * @return a - (b / 2)
     */
    public static int getCenter(int a, int b) {
        return a - (b / 2);
    }

    /**
     * Returns the x position for the given width to appear centered on the screen
     */
    public static int getCenteredX(int width) {
        return getCenter(Game.WINDOW_WIDTH / 2, width);
    }

    /**
     * Returns the y position for the given height to appear centered on the screen
     */
    public static int getCenteredY(int height) {
        return getCenter(Game.WINDOW_HEIGHT / 2, height);
    }

    /**
     * Returns whether or not the first entity is within "tolerance", or distance of, the second entity, in the
     * specified direction
     */
    public static boolean isEntityWithinTolerance(EntityBoundary e1, EntityBoundary e2, Direction direction, int tolerance) {
        switch (direction) {
            case UP:
                return e2.top - tolerance <= e1.bottom && e1.top < e2.top && e1.bottom - e2.top <= tolerance;
            case DOWN:
                return e2.bottom + tolerance >= e1.top && e1.bottom > e2.bottom && e2.bottom - e1.top <= tolerance;
            case LEFT:
                return e2.left <= e1.right + tolerance && e1.left < e2.left && e1.right - e2.left <= tolerance;
            case RIGHT:
                return e2.right + tolerance >= e1.left && e1.left > e2.left && e2.right - e1.left <= tolerance;
            default:
                return false;
        }
    }

    /**
     * Returns whether or not the first entity is within "tolerance", or distance of, the second entity, in the
     * specified direction, using the entities' default boundaries
     */
    public static boolean isEntityWithinTolerance(Entity baseEntity, Entity targetEntity, Direction direction, int tolerance) {
        return isEntityWithinTolerance(baseEntity.defaultBoundary, targetEntity.defaultBoundary, direction, tolerance);
    }

    public static boolean isPointWithinTolerance(int x, int y, int tolerance) {
        return Math.max(x, y) - Math.min(x, y) <= tolerance;
    }

}
