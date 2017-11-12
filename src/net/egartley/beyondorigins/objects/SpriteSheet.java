package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class SpriteSheet {

	private BufferedImage bufferedImage;
	private ArrayList<Sprite> sprites;

	public int spriteSize, rows, columns;

	public SpriteSheet(int size, int r, int c) {
		spriteSize = size;
		rows = r;
		columns = c;
	}

	public void setBufferedImage(BufferedImage image) {
		bufferedImage = image;
	}

	public BufferedImage asBufferedImage() {
		return bufferedImage;
	}

	public ArrayList<Sprite> getAllSprites() {
		return sprites;
	}

}