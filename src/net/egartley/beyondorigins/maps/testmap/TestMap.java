package net.egartley.beyondorigins.maps.testmap;

import java.awt.Graphics;

import net.egartley.beyondorigins.maps.testmap.sectors.Sector1;
import net.egartley.beyondorigins.objects.Map;

public class TestMap extends Map {

	public TestMap() {
		currentSector = new Sector1();
	}

	@Override
	public void tick() {
		currentSector.tick();
	}

	@Override
	public void render(Graphics graphics) {
		currentSector.render(graphics);
	}
	
}
