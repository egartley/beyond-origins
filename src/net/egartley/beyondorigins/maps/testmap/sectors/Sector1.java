package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;
import net.egartley.beyondorigins.objects.MapTile;

public class Sector1 extends MapSector {
	
	public Sector1(MapSectorDefinition def) {
		super(def);
	}

	@Override
	public void render(Graphics graphics) {
		int initialY = 50, initialX = 150, changeY = 0, changeX = 0, intervalX = 0, intervalY = 0;
		for (ArrayList<MapTile> row : definition.tiles) {
			for (MapTile tile : row) {
				intervalX = tile.width;
				intervalY = tile.height;
				graphics.drawImage(tile.bufferedImage, initialX + changeX, initialY + changeY, null);
				changeX += intervalX;
			}
			changeX = 0;
			changeY += intervalY;
		}
		Entities.PLAYER.render(graphics);
	}

	@Override
	public void tick() {
		Entities.PLAYER.tick();
	}

	@Override
	public void enter() {
		Entities.PLAYER.absoluteX = 100;
		Entities.PLAYER.absoluteY = 100;
	}

	@Override
	public void exit() {
		
	}

}