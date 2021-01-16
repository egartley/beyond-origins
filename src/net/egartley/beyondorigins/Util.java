package net.egartley.beyondorigins;

import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Util {

    /**
     * Stitches two images together, side by side
     *
     * @param base     The image on the left
     * @param toStitch The image on the right
     * @return The resulting combination of both images
     */
    public static Image stitchImage(Image base, Image toStitch) {
        int width = base.getWidth() + toStitch.getWidth();
        int height = Math.max(base.getHeight(), toStitch.getHeight());
        try {
            Image stitched = new Image(width, height);
            Graphics graphics = stitched.getGraphics();
            graphics.setBackground(new Color(0, 0, 0, 0));
            graphics.drawImage(base, 0, 0);
            graphics.drawImage(toStitch, base.getWidth(), 0);
            graphics.flush();
            return stitched;
        } catch (SlickException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds an array of images to use for the frames of an animation
     *
     * @param sprite The sprite to get frames from
     * @return An array of images, each representing a single frame
     */
    public static Image[] getAnimationFrames(Sprite sprite) {
        Image[] frames = new Image[sprite.frames.size()];
        Object[] a = sprite.frames.toArray();
        for (int i = 0; i < sprite.frames.size(); i++) {
            frames[i] = (Image) a[i];
        }
        return frames;
    }

    /**
     * Builds an animation from the given image and supplied parameters
     *
     * @param imageStore The number representing the image to retrieve
     * @param width      Width of the animation
     * @param height     Height of the animation
     * @param rows       Number of rows in the image
     * @param frames     Number of frames
     * @param frameDelay How long to wait between frames
     * @param rowOffset  The row to get from the resulting image
     * @return An animation built from the image
     */
    public static Animation getTemplateAnimation(byte imageStore, int width, int height, int rows, int frames, int frameDelay, int rowOffset) {
        return new Animation(getAnimationFrames(new SpriteSheet(Images.get(imageStore), width, height, rows, frames).sprites.get(rowOffset)), frameDelay);
    }

    /**
     * Returns a random integer between the minimum and maximum
     *
     * @param min The smallest value (inclusive)
     * @param max The largest value (exclusive)
     * @return An integer between the minimum and maximum
     */
    public static int randomInt(int min, int max) {
        if (max < min) {
            // if max/min were mixed up, switch them
            int actualmin = max;
            max = min;
            min = actualmin;
        }
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * Returns a random integer between the minimum and maximum
     *
     * @param min       The smallest value (inclusive)
     * @param max       The largest value
     * @param inclusive Whether or not the maximum value could be returned
     * @return An integer between the minimum and maximum
     */
    public static int randomInt(int min, int max, boolean inclusive) {
        if (inclusive) {
            return randomInt(min, max + 1);
        } else {
            return randomInt(min, max);
        }
    }

    /**
     * Returns true the given percentage of the time
     *
     * @param percent The percent chance that true will be returned
     * @return True the given percentage of the time, false otherwise
     */
    public static boolean percentChance(double percent) {
        // assert percent <= 1.0D;
        return randomInt(100, 1, true) <= percent * 100;
    }

    /**
     * Calls {@link #percentChance(double)} with an equal chance of either true or false
     *
     * @return True 50% of the time, false the other 50%
     */
    public static boolean fiftyFifty() {
        return percentChance(0.5D);
    }

    /**
     * Generates an array list of collisions between the base boundary and all of the entity's boundaries
     *
     * @param baseEvent    The collision to use for {@link EntityEntityCollision#start(EntityEntityCollisionEvent)} and {@link EntityEntityCollision#start(EntityEntityCollisionEvent)}
     * @param entity       The entity to generate collisions for all its boundaries
     * @param baseBoundary The other boundary to use for the generated collisions
     * @return Collisions based on the given collision and boundary, and all of the entity's boundaries
     */
    public static ArrayList<EntityEntityCollision> getAllBoundaryCollisions(EntityEntityCollision baseEvent, Entity entity, EntityBoundary baseBoundary) {
        ArrayList<EntityEntityCollision> collisions = new ArrayList<>();
        for (EntityBoundary boundary : entity.boundaries) {
            EntityEntityCollision c = new EntityEntityCollision(boundary, baseBoundary) {
                public void start(EntityEntityCollisionEvent event) {
                    baseEvent.start(event);
                }

                public void end(EntityEntityCollisionEvent event) {
                    baseEvent.end(event);
                }
            };
            c.isMovementRestricting = baseEvent.isMovementRestricting;
            collisions.add(c);
        }
        return collisions;
    }

    /**
     * Annuls, or "cancels," the movement restrictions specified by the collision event
     *
     * @param event  The collision event to annul
     * @param entity The entity to modify movement restrictions on
     */
    public static void annulCollisionEvent(EntityEntityCollisionEvent event, Entity entity) {
        // check for other movement restrictions
        for (EntityEntityCollision c : Collisions.concurrent(entity)) {
            if (c.lastEvent == null) {
                continue;
            }
            if (c.lastEvent.collidedSide == event.collidedSide && c.lastEvent.invoker != event.invoker) {
                // there is another collision that has the same movement
                // restriction, so don't annul it
                return;
            }
        }
        switch (event.collidedSide) {
            case EntityEntityCollisionEvent.TOP_SIDE:
                entity.isAllowedToMoveDownwards = true;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                entity.isAllowedToMoveUpwards = true;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                entity.isAllowedToMoveRightwards = true;
                break;
            case EntityEntityCollisionEvent.RIGHT_SIDE:
                entity.isAllowedToMoveLeftwards = true;
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
     *
     * @param event  The collision event that occured
     * @param entity The entity to modify movement "allowing" for
     */
    public static void onCollisionWithNonTraversableEntity(EntityEntityCollisionEvent event, Entity entity) {
        entity.lastCollisionEvent = event;
        switch (event.collidedSide) {
            case EntityEntityCollisionEvent.RIGHT_SIDE:
                entity.isAllowedToMoveLeftwards = false;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                entity.isAllowedToMoveRightwards = false;
                break;
            case EntityEntityCollisionEvent.TOP_SIDE:
                entity.isAllowedToMoveDownwards = false;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                entity.isAllowedToMoveUpwards = false;
                break;
            default:
                break;
        }
    }

    /**
     * Returns an array containing the given dialogue split into seperate lines, wrapped at words
     *
     * @param dialogue      The entire dialogue to split into lines
     * @param font          The font being used to render these lines
     * @param maxLineLength The maximum length, in pixels (not characters), that a line can be before being wrapped
     * @return An array of strings, each containing a line split from the full dialogue
     */
    public static String[] toLines(String dialogue, Font font, int maxLineLength) {
        ArrayList<String> splits = new ArrayList<>();

        // lazy way to figure out how many characters fit within the specificed max line length
        int lineCheckSize = 0;
        String check = "";
        while (font.getWidth(check) < maxLineLength) {
            check += "_";
        }
        lineCheckSize = check.length();

        if (font.getWidth(dialogue) > maxLineLength) {
            while (font.getWidth(dialogue) > maxLineLength) {
                String line = dialogue.substring(0, lineCheckSize);
                if (line.startsWith(" ")) {
                    // starts with space, probably from a previous split from within a word
                    line = line.substring(1);
                } else if (line.endsWith(" ")) {
                    // ends with space at max length
                    line = line.substring(0, line.length() - 1);
                }
                if (dialogue.indexOf(line) + line.length() + 1 < dialogue.length()
                        && !dialogue.substring(dialogue.indexOf(line) + line.length(), dialogue.indexOf(line) + line.length() + 1).equals(" ")) {
                    // max length within a word, so split from preceding
                    line = line.substring(0, line.lastIndexOf(" "));
                }
                splits.add(line);
                if (dialogue.length() == line.length()) {
                    break;
                }
                dialogue = dialogue.substring(line.length() + 1);
            }
            if (!dialogue.isEmpty() && !dialogue.equals(" ")) {
                // add any remaining dialogue that isn't just a space
                if (dialogue.startsWith(" ")) {
                    // check for extra space at start
                    dialogue = dialogue.substring(1);
                }
                if (!splits.contains(dialogue)) {
                    // check for rare case of duplicates
                    splits.add(dialogue);
                }
            }
        } else {
            return new String[]{dialogue};
        }

        return splits.toArray(new String[]{});
    }

    /**
     * Returns whether or not the specified point is "within bounds," or overlapping, the specified area
     *
     * @param pointX  The x-coordinate of the point
     * @param pointY  The y-coordinate of the point
     * @param boundsX The x-coordinate of the bounds
     * @param boundsY The y-coordinate of the bounds
     * @param width   The width of the bounds
     * @param height  The height of the bounds
     * @return Whether or not the point is located in the bounds
     */
    public static boolean isWithinBounds(int pointX, int pointY, int boundsX, int boundsY, int width, int height) {
        return pointX >= boundsX && pointX <= boundsX + width && pointY >= boundsY && pointY <= boundsY + height;
    }

}
