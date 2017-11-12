package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpriteSheet {

	private BufferedImage fullImage;
	private ArrayList<Sprite> sprites;

	public int spriteWidth, spriteHeight, rows, stripWidth;

	public SpriteSheet(int w, int h, int r, int frames) {
		spriteWidth = w;
		spriteHeight = h;
		rows = r;
		stripWidth = spriteWidth * frames;
		loadAllSprites(frames);
	}
	
	public SpriteSheet(BufferedImage image, int w, int h, int r, int frames) {
		fullImage = image;
		spriteWidth = w;
		spriteHeight = h;
		rows = r;
		stripWidth = spriteWidth * frames;
		loadAllSprites(frames);
	}

	public void setFullImage(BufferedImage image) {
		fullImage = image;
	}
	
	private void loadAllSprites(int frames) {
		sprites = new ArrayList<Sprite>(rows);
		for (int i = 0; i < rows; i++) {
			Sprite s = new Sprite(getBufferedImageStrip(i), spriteWidth, spriteHeight);
			s.setFrames(frames);
			sprites.add(s);
		}
	}

	public BufferedImage getBufferedImageStrip(int rowIndex) {
		return asBufferedImage().getSubimage(0, rowIndex * spriteHeight, stripWidth, spriteHeight);
	}
	
	public BufferedImage asBufferedImage() {
		return fullImage;
	}
	
	public Sprite getSprite(int index) {
		return sprites.get(index);
	}
	
	public ArrayList<Sprite> getSprites() {
		return sprites;
	}

}