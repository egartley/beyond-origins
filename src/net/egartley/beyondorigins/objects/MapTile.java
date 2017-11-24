package net.egartley.beyondorigins.objects;

import java.awt.image.BufferedImage;

public class MapTile {

	public BufferedImage bufferedImage;
	public int width, height;
	public boolean isTraversable;
	
	public MapTile(BufferedImage image) {
		bufferedImage = image;
		width = image.getWidth();
		height = image.getHeight();
	}
	
}
