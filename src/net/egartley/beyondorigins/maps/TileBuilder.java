package net.egartley.beyondorigins.maps;

import net.egartley.beyondorigins.objects.MapTile;

import java.util.ArrayList;

public class TileBuilder {

    /**
     * Returns an array list of array lists of all the same map tiles with the specified ID
     */
    public static ArrayList<ArrayList<MapTile>> buildArrayList(byte tileID, int rows, int columns) {
        byte[][] array = new byte[rows][columns];
        for (int r = 0; r < rows; r++) {
            byte[] col = new byte[columns];
            for (int c = 0; c < columns; c++) {
                col[c] = tileID;
            }
            array[r] = col;
        }
        return buildArrayList(array);
    }

    /**
     * Returns an array list of array lists corresponding to the given 2D array of tile ID's
     */
    private static ArrayList<ArrayList<MapTile>> buildArrayList(byte[][] b) {
        ArrayList<ArrayList<MapTile>> array = new ArrayList<>();
        for (byte[] r : b) {
            ArrayList<MapTile> row = new ArrayList<>();
            for (byte c : r) {
                switch (c) {
                    case MapTile.GRASS:
                        row.add(MapTile.get(MapTile.GRASS));
                        break;
                    case MapTile.SAND:
                        row.add(MapTile.get(MapTile.SAND));
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
