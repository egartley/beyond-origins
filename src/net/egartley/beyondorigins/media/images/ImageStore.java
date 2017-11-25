package net.egartley.beyondorigins.media.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageStore {

	public static String imagePath = "resources/images/";
	public static String mapTilePath = imagePath + "map-tiles/";
	public static BufferedImage grassDefault, sandDefault, stoneDefault;
	public static BufferedImage playerDefault, dummy, tree1;
	
	public static void loadAll() {
		try {
			playerDefault = ImageIO.read(new File(imagePath + "player-default.png"));
			dummy = ImageIO.read(new File(imagePath + "dummy.png"));
			tree1 = ImageIO.read(new File(imagePath + "tree-default.png"));
			
			grassDefault = ImageIO.read(new File(mapTilePath + "grass-default.png"));
			sandDefault = ImageIO.read(new File(mapTilePath + "sand-default.png"));
			stoneDefault = ImageIO.read(new File(mapTilePath + "stone-default.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}