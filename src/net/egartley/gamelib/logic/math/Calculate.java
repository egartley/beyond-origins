package net.egartley.gamelib.logic.math;

import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.objects.Entity;

public class Calculate {

    /**
     * Returns the "center" of b from a
     *
     * @param a
     *         Base x-axis or y-axis coordinate
     * @param b
     *         Width or height
     *
     * @return a - (b / 2)
     */
    public static int getCenter(int a, int b) {
        return a - (b / 2);
    }

    /**
     * Returns whether or not the first entity is within "tolerance", or distance of, the second entity, in the
     * specified direction
     */
    public static boolean isEntityWithinToleranceOf(EntityBoundary e1, EntityBoundary e2, byte direction,
                                                    int tolerance) {
        switch (direction) {
            case Entity.UP:
                return e2.top - tolerance <= e1.bottom && e1.top < e2.top && e1.bottom - e2.top <= tolerance;
            case Entity.DOWN:
                return e2.bottom + tolerance >= e1.top && e1.bottom > e2.bottom && e2.bottom - e1.top <= tolerance;
            case Entity.LEFT:
                return e2.left <= e1.right + tolerance && e1.left < e2.left && e1.right - e2.left <= tolerance;
            case Entity.RIGHT:
                return e2.right + tolerance >= e1.left && e1.left > e2.left && e2.right - e1.left <= tolerance;
            default:
                return false;
        }
    }

    /**
     * Returns whether or not the first entity is within "tolerance", or distance of, the second entity, in the
     * specified direction (uses each entity's {@link Entity#defaultBoundary})
     */
    public static boolean isEntityWithinToleranceOf(Entity baseEntity, Entity targetEntity, byte direction,
                                                    int tolerance) {
        return isEntityWithinToleranceOf(baseEntity.defaultBoundary, targetEntity.defaultBoundary, direction,
                tolerance);
    }

}