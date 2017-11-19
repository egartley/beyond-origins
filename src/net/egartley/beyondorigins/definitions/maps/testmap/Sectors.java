package net.egartley.beyondorigins.definitions.maps.testmap;

import java.util.ArrayList;

import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.beyondorigins.objects.MapSectorDefinition;
import net.egartley.beyondorigins.objects.MapTile;

public class Sectors {

	public static MapSectorDefinition sector1, sector2;

	private static MapTile grass, sand, stone;

	private static void loadTiles() {
		grass = new MapTile(ImageStore.grassDefault);
		sand = new MapTile(ImageStore.sandDefault);
		stone = new MapTile(ImageStore.stoneDefault);
	}

	public static void defineAll() {
		loadTiles();

		// sector1
		byte[][] b1 = new byte[][] {
				{ 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 1, 1, 1, 1, 1, 1, 0 },
				{ 0, 1, 2, 2, 2, 2, 1, 0 },
				{ 0, 1, 2, 0, 0, 2, 1, 0 },
				{ 0, 1, 2, 0, 0, 2, 1, 0 },
				{ 0, 1, 2, 2, 2, 2, 1, 0 },
				{ 0, 1, 1, 1, 1, 1, 1, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }};
		sector1 = new Sector1Definition(buildArrayList(b1), stone);

		// sector2
	}

	private static ArrayList<ArrayList<MapTile>> buildArrayList(byte[][] b) {
		ArrayList<ArrayList<MapTile>> array = new ArrayList<ArrayList<MapTile>>();
		for (byte i = 0; i < b.length; i++) {
			ArrayList<MapTile> row = new ArrayList<MapTile>();
			for(byte j = 0; j < b[i].length; j++) {
				switch (b[i][j]) {
				case 0:
					row.add(grass);
					break;
				case 1:
					row.add(sand);
					break;
				case 2:
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
