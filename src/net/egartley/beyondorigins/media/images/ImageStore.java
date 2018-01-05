package net.egartley.beyondorigins.media.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.egartley.beyondorigins.Debug;

public class ImageStore {

	public static String path = "resources/images/";
	public static String mapTilePath = path + "map-tiles/";
	public static String entityPath = path + "entities/";
	public static BufferedImage grassDefault;
	public static BufferedImage sandDefault;
	public static BufferedImage playerDefault;
	public static BufferedImage dummy;
	public static BufferedImage treeDefault;
	public static BufferedImage rockDefault;

	public static void loadAll() {
		try {
			playerDefault = ImageIO.read(new File(entityPath + "player-default.png"));
			dummy = ImageIO.read(new File(entityPath + "dummy.png"));
			treeDefault = ImageIO.read(new File(entityPath + "tree-default.png"));
			rockDefault = ImageIO.read(new File(entityPath + "rock-default.png"));

			grassDefault = ImageIO.read(new File(mapTilePath + "grass-default.png"));
			sandDefault = ImageIO.read(new File(mapTilePath + "sand-default.png"));
		} catch (IOException e) {
			Debug.error("There was an issue while trying to load images");
			e.printStackTrace();
		}
	}

}
