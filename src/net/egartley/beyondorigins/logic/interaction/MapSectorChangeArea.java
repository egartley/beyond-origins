package net.egartley.beyondorigins.logic.interaction;

import java.awt.Color;

import net.egartley.beyondorigins.objects.MapSector;

/**
 * An area (boundary) where a map sector change can occur
 * 
 * @author Evan Gartley
 */
public class MapSectorChangeArea extends Boundary {

	public MapSector to;

	public MapSectorChangeArea(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		top = y;
		bottom = top + height;
		left = x;
		right = left + width;
		drawColor = Color.MAGENTA;
	}

	@Override
	public void tick() {

	}

}
