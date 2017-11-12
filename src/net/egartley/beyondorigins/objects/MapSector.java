package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class MapSector {

	public ArrayList<ArrayList<MapTile>> tileArray;
	public MapTile borderTile;
	public abstract void render(Graphics graphics);
	public abstract void tick();
	
}