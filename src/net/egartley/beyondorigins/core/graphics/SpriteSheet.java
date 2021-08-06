package net.egartley.beyondorigins.core.graphics;

import org.newdawn.slick.Image;

import java.util.ArrayList;

/**
 * An image that contains multiple sprites, each represented by a row, or "strip" that is specified in the constructor
 */
public class SpriteSheet {

    private final int rows;
    private final int frames;
    private final int frameWidth;
    private final int frameHeight;
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

    public SpriteSheet(Image image, int frameWidth, int frameHeight, int rows, int frames) {
        sheet = image;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.rows = rows;
        this.frames = frames;
        loadSprites();
    }

    private void loadSprites() {
        sprites = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            sprites.add(new Sprite(getRow(i), frameWidth, frameHeight, frames));
        }
    }

    private Image getRow(int index) {
        return sheet.getSubImage(0, index * frameHeight, sheet.getWidth(), frameHeight);
    }

    public Sprite getSprite(int index) {
        if (index >= sprites.size() || index < 0) {
            return null;
        }
        return sprites.get(index);
    }

}
