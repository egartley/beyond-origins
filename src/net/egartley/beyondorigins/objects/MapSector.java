package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class MapSector {

	public MapSector(MapSectorDefinition def) {
		definition = def;
	}
	
	public MapSectorDefinition definition;
	public abstract void render(Graphics graphics);
	public abstract void tick();
	public abstract void onEnter();
	public abstract void onExit();
	
	public void drawTiles(Graphics graphics, int ix, int iy) {
		int changeY = 0, changeX = 0;
		for (ArrayList<MapTile> row : definition.tiles) {
			for (MapTile tile : row) {
				graphics.drawImage(tile.bufferedImage, ix + changeX, iy + changeY, null);
				changeX += 32;
			}
			changeX = 0;
			changeY += 32;
		}
	}
	
}