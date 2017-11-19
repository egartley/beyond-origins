package net.egartley.beyondorigins.maps.testmap;

import java.awt.Graphics;

import net.egartley.beyondorigins.maps.testmap.sectors.Sector1;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;

public class TestMap extends Map {

	public MapSector sector1;
	
	public TestMap() {
		sector1 = new Sector1(net.egartley.beyondorigins.definitions.maps.testmap.Sectors.sector1);
		changeSector(sector1);
	}

	@Override
	public void tick() {
		currentSector.tick();
	}

	@Override
	public void render(Graphics graphics) {
		currentSector.render(graphics);
	}

	@Override
	public void changeSector(MapSector sector) {
		currentSector = sector;
	}

}
