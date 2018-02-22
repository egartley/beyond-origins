package net.egartley.beyondorigins.logic.interaction;

import java.awt.Color;

import net.egartley.beyondorigins.objects.MapSector;

/**
 * An area (boundary) where a map sector change can occur
 * 
 * @author Evan Gartley
 */
public class MapSectorChangeBoundary extends Boundary {

	/**
	 * The map sector where the player will be going to after colliding with this
	 * boundary
	 */
	public MapSector goingTo;

	public MapSectorChangeBoundary(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		top = y;
		bottom = top + height;
		left = x;
		right = left + width;
		drawColor = Color.ORANGE;
	}

	@Override
	public void tick() {

	}

}
