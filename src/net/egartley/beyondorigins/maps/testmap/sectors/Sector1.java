package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Color;
import java.awt.Graphics;

import net.egartley.beyondorigins.objects.MapSector;

public class Sector1 extends MapSector {

	@Override
	public void render(Graphics graphics) {
		graphics.setColor(Color.YELLOW);
		graphics.fillOval(16, 16, 100, 100);
	}

	@Override
	public void tick() {
		
	}

}