package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Graphics;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;

public class Sector2 extends MapSector {

	public Sector2(Map parent, MapSectorDefinition def) {
		super(parent, def);
	}

	@Override
	public void render(Graphics graphics) {
		drawTiles(graphics);
		Entities.PLAYER.render(graphics);
	}

	@Override
	public void tick() {
		Entities.PLAYER.tick();
	}

	@Override
	public void onPlayerEnter() {
		// set default/initial position
		Entities.PLAYER.x = Game.WINDOW_WIDTH / 2 - (Entities.PLAYER.sprite.frameWidth / 2);
		Entities.PLAYER.y = Game.WINDOW_HEIGHT - 100;
	}

	@Override
	public void onPlayerLeave() {

	}

}
