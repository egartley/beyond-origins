package net.egartley.beyondorigins.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * An image that contains multiple sprites, each represented by a row, or "strip" that is specified in the constructor
 *
 * @see Sprite
 */
public class SpriteSheet {

    private int spriteWidth;
    private int spriteHeight;
    private int strips;
    private int stripWidth;
    private int frames;

    private BufferedImage sheet;

    public ArrayList<Sprite> sprites;

    public SpriteSheet(BufferedImage sheet, int rows, int frames) {
        this(sheet, sheet.getWidth(), sheet.getHeight(), rows, frames);
    }

    public SpriteSheet(BufferedImage image, int width, int height, int rows, int frames) {
        sheet = image;
        spriteWidth = width;
        spriteHeight = height;
        strips = rows;
        this.frames = frames;
        stripWidth = spriteWidth * frames;
        load();
    }

    /**
     * Builds the sprite collection from the sheet image (should have already been set)
     */
    private void load() {
        sprites = new ArrayList<>(strips);
        for (int i = 0; i < strips; i++) {
            // get each row as a sprite, then add to collection
            sprites.add(new Sprite(getRow(i), spriteWidth, spriteHeight, frames));
        }
    }

    /**
     * Returns the "strip" at the given index
     *
     * @param index
     *         The row number, or index, of the "strip" to return in the strip
     */
    private BufferedImage getRow(int index) {
        return sheet.getSubimage(0, index * spriteHeight, stripWidth, spriteHeight);
    }

    public Sprite getSprite(int index) {
        if (index >= sprites.size())
            return null;
        return sprites.get(index);
    }

}
