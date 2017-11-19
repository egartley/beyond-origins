package net.egartley.beyondorigins.objects;

import java.awt.Graphics;

public abstract class MapSector {

	public MapSector(MapSectorDefinition def) {
		definition = def;
	}
	
	public MapSectorDefinition definition;
	public abstract void render(Graphics graphics);
	public abstract void tick();
	public abstract void enter();
	public abstract void exit();
	
}