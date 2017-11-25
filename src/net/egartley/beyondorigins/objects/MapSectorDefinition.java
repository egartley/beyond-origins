package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

public class MapSectorDefinition {
	
	public MapSectorDefinition(ArrayList<ArrayList<MapTile>> tileArray) {
		tiles = tileArray;
	}
	
	public ArrayList<ArrayList<MapTile>> tiles;
	
}