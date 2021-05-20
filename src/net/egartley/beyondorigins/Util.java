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
     * Cancels the movement restrictions specified by the collision event
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
            case TOP:
                entity.isAllowedToMoveDownwards = true;
                break;
            case BOTTOM:
                entity.isAllowedToMoveUpwards = true;
                break;
            case LEFT:
                entity.isAllowedToMoveRightwards = true;
                break;
            case RIGHT:
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
     */
    public static void onCollisionWithNonTraversableEntity(EntityEntityCollisionEvent event, Entity entity) {
        entity.lastCollisionEvent = event;
        switch (event.collidedSide) {
            case RIGHT:
                entity.isAllowedToMoveLeftwards = false;
                break;
            case LEFT:
                entity.isAllowedToMoveRightwards = false;
                break;
            case TOP:
                entity.isAllowedToMoveDownwards = false;
                break;
            case BOTTOM:
                entity.isAllowedToMoveUpwards = false;
                break;
            default:
                break;
        }
    }

    /**
     * Returns a random integer between the minimum and maximum
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
     */
    public static int randomInt(int min, int max, boolean inclusive) {
        if (inclusive) {
            return randomInt(min, max + 1);
        } else {
            return randomInt(min, max);
        }
    }

    /**
     * Calls {@link #percentChance(double)} with an equal chance of either true or false
     */
    public static boolean fiftyFifty() {
        return percentChance(0.5D);
    }

    /**
     * Returns true the given percentage of the time
     */
    public static boolean percentChance(double percent) {
        if (percent > 1.0D) {
            percent = 1.0D;
        }
        return randomInt(100, 1, true) <= percent * 100;
    }

    /**
     * Returns whether or not the specified point is overlapping the specified area
     */
    public static boolean isWithinBounds(int pointX, int pointY, int boundsX, int boundsY, int width, int height) {
        return pointX >= boundsX && pointX <= boundsX + width && pointY >= boundsY && pointY <= boundsY + height;
    }

    /**
     * Stitches two images together, side by side
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
     * Returns an array containing the given dialogue split into seperate lines, wrapped at words
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
     * Builds an animation from the given image and supplied parameters
     */
    public static Animation getTemplateAnimation(byte imageStore, int width, int height, int rows, int frames, int frameDelay, int rowOffset) {
        return new Animation(getAnimationFrames(new SpriteSheet(Images.get(imageStore), width, height, rows, frames).sprites.get(rowOffset)), frameDelay);
    }

    /**
     * Generates an array list of collisions between the base boundary and all of the entity's boundaries
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

}
