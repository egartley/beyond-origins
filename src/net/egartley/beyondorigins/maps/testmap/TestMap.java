package net.egartley.beyondorigins.maps.testmap;

import java.awt.Graphics;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.maps.testmap.sectors.Sector1;
import net.egartley.beyondorigins.objects.Map;

public class TestMap extends Map {

	public TestMap() {
		currentSector = new Sector1();
		Entities.PLAYER.absoluteX = 100;
		Entities.PLAYER.absoluteY = 100;
	}

	@Override
	public void tick() {
		currentSector.tick();
		Entities.PLAYER.tick();
	}

	@Override
	public void render(Graphics graphics) {
		currentSector.render(graphics);
		Entities.PLAYER.render(graphics);
	}
	
}
