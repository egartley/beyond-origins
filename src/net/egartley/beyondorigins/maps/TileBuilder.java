package net.egartley.beyondorigins.maps;

import java.util.ArrayList;

import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.beyondorigins.objects.MapTile;

public class TileBuilder {

	public static final byte GRASS = 0;
	public static final byte SAND = 1;

	public static MapTile grass;
	public static MapTile sand;

	public static void load() {
		grass = new MapTile(ImageStore.grassDefault);
		sand = new MapTile(ImageStore.sandDefault);
	}
	
	public static ArrayList<ArrayList<MapTile>> buildArrayList(byte[][] b) {
		ArrayList<ArrayList<MapTile>> array = new ArrayList<ArrayList<MapTile>>();
		for (byte i = 0; i < b.length; i++) {
			ArrayList<MapTile> row = new ArrayList<MapTile>();
			for (byte j = 0; j < b[i].length; j++) {
				switch (b[i][j]) {
				case GRASS:
					row.add(grass);
					break;
				case SAND:
					row.add(sand);
					break;
				default:
					break;
				}
			}
			array.add(row);
		}
		return array;
	}

}
