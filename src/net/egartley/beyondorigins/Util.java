package net.egartley.beyondorigins;

import net.egartley.beyondorigins.interfaces.Collidable;
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
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    /**
     * Returns a re-sized image of the original, at the given width and height
     */
    static BufferedImage resize(BufferedImage image, int width, int height) {
        return resize(image, width, height, Image.SCALE_DEFAULT);
    }

    /**
     * Returns a re-sized image of the original, at the given width and height
     */
    static BufferedImage resize(BufferedImage image, int width, int height, int hints) {
        return toBufferedImage(image.getScaledInstance(width, height, hints));
    }

    /**
     * Returns a random integer between the supplied maximum and minimum values
     */
    private static int randomInt(int maximum, int minimum) {
        return ThreadLocalRandom.current().nextInt(minimum, maximum);
    }

    /**
     * Returns a random integer between the supplied maximum and minimum values
     */
    public static int randomInt(int maximum, int minimum, boolean inclusive) {
        if (inclusive) {
            return randomInt(maximum + 1, minimum);
        } else {
            return randomInt(maximum, minimum);
        }
    }

    /**
     * Returns generated entity-to-entity collisions based on the given event, entity and boundary
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
            for (EntityEntityCollision c : ((Collidable) e).collisions) {
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

    /**
     * Annuls, or "cancels," the specified entity-entity collision event on the specified entity
     */
    public static void annulCollisionEvent(EntityEntityCollisionEvent event, Entity e) {
        // check for other movement restrictions
        for (EntityEntityCollision c : ((Collidable) e).concurrentCollisions) {
            if (c.lastEvent.collidedSide == event.collidedSide && c.lastEvent.invoker != event.invoker) {
                // there is another collision that has the same movement
                // restriction, so don't annul it
                return;
            }
        }
        switch (event.collidedSide) {
            case EntityEntityCollisionEvent.TOP_SIDE:
                e.isAllowedToMoveDownwards = true;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                e.isAllowedToMoveUpwards = true;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                e.isAllowedToMoveRightwards = true;
                break;
            case EntityEntityCollisionEvent.RIGHT_SIDE:
                e.isAllowedToMoveLeftwards = true;
                break;
            default:
                break;
        }
    }

    /**
     * Disallows movement opposite of the side of the collision. For
     * example, if the player collided with a tree on its top side,
     * downward movement would no longer be allowed until that collision
     * is no longer active
     */
    public static void onCollisionWithNonTraversableEntity(EntityEntityCollisionEvent event, Entity e) {
        e.lastCollisionEvent = event;
        switch (event.collidedSide) {
            case EntityEntityCollisionEvent.RIGHT_SIDE:
                // collided on the right, so disable leftwards movement
                e.isAllowedToMoveLeftwards = false;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                // collided on the left, so disable rightwards movement
                e.isAllowedToMoveRightwards = false;
                break;
            case EntityEntityCollisionEvent.TOP_SIDE:
                // collided at the top, so disable downwards movement
                e.isAllowedToMoveDownwards = false;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                // collided at the bottom, so disable upwards movement
                e.isAllowedToMoveUpwards = false;
                break;
            default:
                break;
        }
    }

    /**
     * Returns whether or not the specified point is "within bounds," or overlapping, the specified area
     */
    public static boolean isWithinBounds(int pointX, int pointY, int x, int y, int width, int height) {
        return pointX >= x && pointX <= x + width && pointY >= y && pointY <= y + height;
    }

}
