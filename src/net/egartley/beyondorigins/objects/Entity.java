package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

public abstract class Entity {

	private ArrayList<Sprite> sprites;
	private boolean isAnimated;
	private boolean isSingleFrame;

	public Entity(ArrayList<Sprite> sprites) {
		this.sprites = sprites;
		isSingleFrame = sprites.size() == 1;
	}

	public Entity(Sprite sprite) {
		sprites = new ArrayList<Sprite>();
		sprites.add(sprite);
		isSingleFrame = true;
	}

	public ArrayList<Sprite> getSprites() {
		return sprites;
	}

	public boolean getIsAnimated() {
		return isAnimated;
	}

	public boolean getIsSingleFrame() {
		return isSingleFrame;
	}

}
