package net.egartley.beyondorigins.core.graphics;

import org.newdawn.slick.Image;

import java.util.ArrayList;

/**
 * An image that contains multiple sprites, each represented by a row, or "strip" that is specified in the constructor
 *
 * @see Sprite
 */
public class SpriteSheet {

    private final int rows;
    private final int frames;
    private final int spriteWidth;
    private final int spriteHeight;
    private final Image sheet;

    public ArrayList<Sprite> sprites;

    public SpriteSheet(Image sheet) {
        this(sheet, sheet.getWidth());
    }

    public SpriteSheet(Image sheet, int width) {
        this(sheet, 1, sheet.getWidth() / width);
    }

    public SpriteSheet(Image sheet, int rows, int frames) {
        this(sheet, sheet.getWidth(), sheet.getHeight(), rows, frames);
    }

    public SpriteSheet(Image image, int width, int height, int rows, int frames) {
        sheet = image;
        spriteWidth = width;
        spriteHeight = height;
        this.rows = rows;
        this.frames = frames;
        load();
    }

    private void load() {
        sprites = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            sprites.add(new Sprite(getRow(i), spriteWidth, spriteHeight, frames));
        }
    }

    private Image getRow(int index) {
        return sheet.getSubImage(0, index * spriteHeight, sheet.getWidth(), spriteHeight);
    }

    public Sprite getSprite(int index) {
        if (index >= sprites.size() || index < 0) {
            return null;
        }
        return sprites.get(index);
    }

}
