package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

public class MapSectorDefinition {
	
	public MapSectorDefinition(ArrayList<ArrayList<MapTile>> tileArray, MapTile borderTile) {
		tiles = tileArray;
		border = borderTile;
	}
	
	public ArrayList<ArrayList<MapTile>> tiles;
	public MapTile border;
	
}