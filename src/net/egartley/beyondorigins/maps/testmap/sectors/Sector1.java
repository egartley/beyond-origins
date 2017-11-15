package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Graphics;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.objects.MapSector;

public class Sector1 extends MapSector {

	public Sector1() {
		Entities.PLAYER.absoluteX = 100;
		Entities.PLAYER.absoluteY = 100;
	}
	
	@Override
	public void render(Graphics graphics) {
		Entities.PLAYER.render(graphics);
	}

	@Override
	public void tick() {
		Entities.PLAYER.tick();
	}

}