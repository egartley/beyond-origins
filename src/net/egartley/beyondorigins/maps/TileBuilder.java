package net.egartley.beyondorigins.maps;

import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.beyondorigins.objects.MapTile;

import java.util.ArrayList;

public class TileBuilder {

    public static final byte GRASS = 0;
    private static final byte SAND = 1;

    private static MapTile grass;
    private static MapTile sand;

    public static void load() {
        grass = new MapTile(ImageStore.get(ImageStore.TILE_GRASS));
        sand = new MapTile(ImageStore.get(ImageStore.TILE_SAND));
    }

    public static ArrayList<ArrayList<MapTile>> buildArrayList(byte b, int r, int c) {
        byte[][] array = new byte[r][c];
        for (int rr = 0; rr < r; rr++) {
            byte[] col = new byte[c];
            for (int cc = 0; cc < c; cc++)
                col[cc] = b;

            array[rr] = col;
        }
        return buildArrayList(array);
    }

    private static ArrayList<ArrayList<MapTile>> buildArrayList(byte[][] b) {
        ArrayList<ArrayList<MapTile>> array = new ArrayList<>();
        for (byte[] aB : b) {
            ArrayList<MapTile> row = new ArrayList<>();
            for (byte anAB : aB) {
                switch (anAB) {
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
