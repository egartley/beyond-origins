package net.egartley.beyondorigins;

import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.graphics.Sprite;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
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
     */
    public static Image stitchImage(Image base, Image toStitch) {
        int width = base.getWidth() + toStitch.getWidth();
        int height = Math.max(base.getHeight(), toStitch.getHeight());
        try {
            // TODO: look at end of wiki page
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

    public static Image[] getAnimationFrames(Sprite sprite) {
        Image[] frames = new Image[sprite.frames.size()];
        Object[] a = sprite.frames.toArray();
        for (int i = 0; i < sprite.frames.size(); i++) {
            frames[i] = (Image) a[i];
        }
        return frames;
    }

    public static Animation getTemplateAnimation(byte imageStore, int width, int height, int rows, int frames, int frameDelay) {
        return getTemplateAnimation(imageStore, width, height, rows, frames, frameDelay, 0);
    }

    public static Animation getTemplateAnimation(byte imageStore, int width, int height, int rows, int frames, int frameDelay, int rowOffset) {
        return new Animation(getAnimationFrames(new SpriteSheet(Images.get(imageStore), width, height, rows, frames).sprites.get(rowOffset)), frameDelay);
    }

    /**
     * Returns a random integer between the supplied maximum and minimum values
     */
    public static int randomInt(int maximum, int minimum) {
        if (maximum < minimum) {
            // if max/min were mixed up, switch them
            int actualmin = maximum;
            maximum = minimum;
            minimum = actualmin;
        }
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
     * Returns <code>true</code> the given percent of the time.
     * <br><br>Example: <code>percentChance(0.1)</code> would return <code>true</code> 10% of the time
     *
     * @param percent The percent as a decimal (<= 1)
     */
    public static boolean percentChance(double percent) {
        // assert percent <= 1.0D;
        return randomInt(100, 1, true) <= percent * 100;
    }

    /**
     * Returns <code>false</code> 50% of the time, and <code>true</code> 50% of the time
     */
    public static boolean fiftyFifty() {
        return percentChance(0.5D);
    }

    /**
     * Returns generated entity-to-entity collisions based on the given event, entity and boundary
     */
    public static ArrayList<EntityEntityCollision> getAllBoundaryCollisions(EntityEntityCollision baseEvent, Entity
            entity, EntityBoundary baseBoundary) {
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
     */
    public static void annulCollisionEvent(EntityEntityCollisionEvent event, Entity e) {
        // check for other movement restrictions
        for (EntityEntityCollision c : e.concurrentCollisions) {
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
                e.isAllowedToMoveLeftwards = false;
                break;
            case EntityEntityCollisionEvent.LEFT_SIDE:
                e.isAllowedToMoveRightwards = false;
                break;
            case EntityEntityCollisionEvent.TOP_SIDE:
                e.isAllowedToMoveDownwards = false;
                break;
            case EntityEntityCollisionEvent.BOTTOM_SIDE:
                e.isAllowedToMoveUpwards = false;
                break;
            default:
                break;
        }
    }

    /**
     * Returns an array containing the given dialogue split into seperate lines, wrapped at words
     */
    public static String[] toLines(String fullText, Font font, int maxLineLength) {
        ArrayList<String> splits = new ArrayList<>();

        // lazy way to figure out how many characters fit within the specificed max line length
        int lineCheckSize = 0;
        String check = "";
        while (font.getWidth(check) < maxLineLength) {
            check += "_";
        }
        lineCheckSize = check.length();

        if (font.getWidth(fullText) > maxLineLength) {
            while (font.getWidth(fullText) > maxLineLength) {
                String line = fullText.substring(0, lineCheckSize);
                if (line.startsWith(" ")) {
                    // starts with space, probably from a previous split from within a word
                    line = line.substring(1);
                } else if (line.endsWith(" ")) {
                    // ends with space at max length
                    line = line.substring(0, line.length() - 1);
                }
                if (fullText.indexOf(line) + line.length() + 1 < fullText.length()
                        && !fullText.substring(fullText.indexOf(line) + line.length(), fullText.indexOf(line) + line.length() + 1).equals(" ")) {
                    // max length within a word, so split from preceding
                    line = line.substring(0, line.lastIndexOf(" "));
                }
                splits.add(line);
                if (fullText.length() == line.length()) {
                    break;
                }
                fullText = fullText.substring(line.length() + 1);
            }
            if (!fullText.isEmpty() && !fullText.equals(" ")) {
                // add any remaining fullText that isn't just a space
                if (fullText.startsWith(" ")) {
                    // check for extra space at start
                    fullText = fullText.substring(1);
                }
                if (!splits.contains(fullText)) {
                    // check for rare case of duplicates
                    splits.add(fullText);
                }
            }
        } else {
            return new String[]{fullText};
        }

        return splits.toArray(new String[]{});
    }

    /**
     * Returns whether or not the specified point is "within bounds," or overlapping, the specified area
     */
    public static boolean isWithinBounds(int pointX, int pointY, int boundsX, int boundsY, int width, int height) {
        return pointX >= boundsX && pointX <= boundsX + width && pointY >= boundsY && pointY <= boundsY + height;
    }

}
