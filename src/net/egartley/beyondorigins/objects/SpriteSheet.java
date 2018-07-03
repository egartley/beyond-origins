package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * An image that contains multiple sprites, each represented by a row, or
 * "strip" that is specified in the constructor
 *
 * @see Sprite
 */
public class SpriteSheet {

    private BufferedImage sheet;

    public ArrayList<Sprite> collection;

    private int spriteWidth;
    private int spriteHeight;
    private int strips;
    private int stripWidth;
    private int frames;

    public SpriteSheet(BufferedImage image, int rows, int frames) {
        this(image, image.getWidth(), image.getHeight(), rows, frames);
    }

    public SpriteSheet(BufferedImage image, int width, int height, int rows, int frames) {
        sheet = image;
        spriteWidth = width;
        spriteHeight = height;
        strips = rows;
        this.frames = frames;
        stripWidth = spriteWidth * frames;
        loadAllSprites();
    }

    /**
     * Builds the sprite collection from the sheet image (should have already been
     * set)
     */
    private void loadAllSprites() {
        collection = new ArrayList<>(strips);
        for (int i = 0; i < strips; i++) {
            // get each row as a sprite, then add to collection
            Sprite s = new Sprite(getStripAsBufferedImage(i), spriteWidth, spriteHeight);
            s.setFrames(frames);
            collection.add(s);
        }
    }

    /**
     * Returns the "strip" at the given row index
     *
     * @param rowIndex The row number, or index, of the "strip" to return in the strip
     * @return The specified "strip" as a BufferedImage
     */
    private BufferedImage getStripAsBufferedImage(int rowIndex) {
        return sheet.getSubimage(0, rowIndex * spriteHeight, stripWidth, spriteHeight);
    }

    public Sprite getSprite(int index) {
        if (index >= collection.size())
            return null;
        return collection.get(index);
    }

}
