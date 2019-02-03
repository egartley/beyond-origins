package net.egartley.beyondorigins;

import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Util {

    // https://stackoverflow.com/a/13605411
    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage)
            return (BufferedImage) img;
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    /**
     * Returns a re-sized image of the original, at the given width and height
     *
     * @param image  The original image to resize (will not be altered)
     * @param width  New width to resize to
     * @param height New height to resize to
     * @return {@link #resize(BufferedImage, int, int, int) resize(image, width, height, Image.SCALE_DEFAULT)}
     */
    static BufferedImage resize(BufferedImage image, int width, int height) {
        return resize(image, width, height, Image.SCALE_DEFAULT);
    }

    /**
     * Returns a re-sized image of the original, at the given width and height
     *
     * @param image  The original image to resize (will not be altered)
     * @param width  New width to resize to
     * @param height New height to resize to
     * @param hints  Scaling algorithm to use (see {@link Image})
     * @return {@link #toBufferedImage(Image) toBufferedImage(image.getScaledInstance(width, height, hints))}
     */
    static BufferedImage resize(BufferedImage image, int width, int height, int hints) {
        return toBufferedImage(image.getScaledInstance(width, height, hints));
    }

    /**
     * Returns a random integer between the supplied maximum and minimum values
     *
     * @param maximum The maximum value
     * @param minimum The minimum value
     */
    private static int randomInt(int maximum, int minimum) {
        return ThreadLocalRandom.current().nextInt(minimum, maximum);
    }

    /**
     * Returns a random integer between the supplied maximum and minimum values
     *
     * @param maximum   The maximum value
     * @param minimum   The minimum value
     * @param inclusive Whether or not to include the maximum as a possible value
     * @return {@link #randomInt(int, int) randomInt(maximum + 1, minimum)}
     */
    public static int randomInt(int maximum, int minimum, boolean inclusive) {
        if (inclusive)
            return randomInt(maximum + 1, minimum);
        else
            return randomInt(maximum, minimum);
    }

    /**
     * Returns generated entity-to-entity collisions based on the given event, entity and boundary
     *
     * @param baseEvent    The {@link EntityEntityCollision} in which to base all of the returned ones on
     * @param entity       The {@link Entity} in which to generate collisions around each of its boundaries
     * @param baseBoundary The other entity's {@link EntityBoundary}
     */
    public static ArrayList<EntityEntityCollision> getAllBoundaryCollisions(EntityEntityCollision baseEvent, Entity
            entity, EntityBoundary baseBoundary) {
        ArrayList<EntityEntityCollision> collisions = new ArrayList<>();
        for (EntityBoundary boundary : entity.boundaries) {
            EntityEntityCollision c = new EntityEntityCollision(boundary, baseBoundary) {
                public void onCollide(EntityEntityCollisionEvent event) {
                    baseEvent.onCollide(event);
                }

                public void onCollisionEnd(EntityEntityCollisionEvent event) {
                    baseEvent.onCollisionEnd(event);
                }
            };
            c.isMovementRestricting = baseEvent.isMovementRestricting;
            collisions.add(c);
        }
        return collisions;
    }

    public static void fixCrossSectorCollisions(ArrayList<Entity> entities) {
        for (Entity e : entities) {
            for (EntityEntityCollision c : e.collisions) {
                if (c.isCollided) {
                    for (EntityBoundary eb : c.boundaries) {
                        if (!eb.parent.isSectorSpecific) {
                            eb.isCollided = false;
                            eb.parent.isCollided = false;
                            eb.setColor();
                        }
                    }
                }
            }
        }
    }

    public static boolean isClickInBounds(int cx, int cy, int x, int y, int width, int height) {
        return cx >= x && cx <= x + width && cy >= y && cy <= y + height;
    }

}
