package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpriteSheet {

	private BufferedImage fullImage;
	private ArrayList<Sprite> sprites;

	public int spriteWidth, spriteHeight, strips, stripWidth;

	public SpriteSheet(int w, int h, int r, int frames) {
		spriteWidth = w;
		spriteHeight = h;
		strips = r;
		stripWidth = spriteWidth * frames;
		loadAllSprites(frames);
	}

	public SpriteSheet(BufferedImage image, int w, int h, int r, int frames) {
		fullImage = image;
		spriteWidth = w;
		spriteHeight = h;
		strips = r;
		stripWidth = spriteWidth * frames;
		loadAllSprites(frames);
	}

	public void setFullImage(BufferedImage image) {
		fullImage = image;
	}

	private void loadAllSprites(int frames) {
		sprites = new ArrayList<Sprite>(strips);
		for (int i = 0; i < strips; i++) {
			Sprite s = new Sprite(getStripAsBufferedImage(i), spriteWidth, spriteHeight);
			s.setFrames(frames);
			sprites.add(s);
		}
	}

	public BufferedImage getStripAsBufferedImage(int rowIndex) {
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