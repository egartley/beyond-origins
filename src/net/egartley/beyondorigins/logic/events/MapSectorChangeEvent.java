package net.egartley.beyondorigins.logic.events;

import net.egartley.beyondorigins.objects.MapSector;

public class MapSectorChangeEvent {

	public MapSector from;
	public MapSector to;
	
	public MapSectorChangeEvent(MapSector from, MapSector to) {
		this.from = from;
		this.to = to;
	}
	
}
