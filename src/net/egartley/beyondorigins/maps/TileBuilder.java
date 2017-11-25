package net.egartley.beyondorigins.maps;

import java.util.ArrayList;

import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.beyondorigins.objects.MapTile;

public class TileBuilder {

	public static final byte GRASS = 0, SAND = 1, STONE = 2;

	public static MapTile grass, sand, stone;

	public static void load() {
		grass = new MapTile(ImageStore.grassDefault);
		sand = new MapTile(ImageStore.sandDefault);
		stone = new MapTile(ImageStore.stoneDefault);
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
				case STONE:
					row.add(stone);
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
