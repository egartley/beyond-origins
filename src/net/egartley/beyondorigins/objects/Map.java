package net.egartley.beyondorigins.objects;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Map {

	public ArrayList<MapSector> sectors;
	public MapSector currentSector;
	public abstract void setSector(MapSector sector);
	public abstract void tick();
	public abstract void render(Graphics graphics);
	
}