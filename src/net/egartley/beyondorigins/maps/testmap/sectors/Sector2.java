package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Graphics;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.MapSectorChangeCollision;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeBoundary;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;

public class Sector2 extends MapSector {

	public Sector2(Map parent, MapSectorDefinition def) {
		super(parent, def, new MapSectorChangeBoundary(0, Game.WINDOW_HEIGHT - 18, Game.WINDOW_WIDTH, 18),
				new MapSectorChangeBoundary(Game.WINDOW_WIDTH - 18, 0, 18, Game.WINDOW_HEIGHT));
	}

	@Override
	public void render(Graphics graphics) {
		super.render(graphics);
		Entities.PLAYER.render(graphics);

		for (MapSectorChangeBoundary boundary : changeBoundaries) {
			boundary.draw(graphics);
		}
	}

	@Override
	public void tick() {
		Entities.PLAYER.tick();

		for (MapSectorChangeCollision collision : changeCollisions) {
			collision.tick();
		}
	}

	@Override
	public void onPlayerEnter(MapSector from) {
		if (from.equals(parent.sectors.get(0))) {
			// from sector 1
			Entities.PLAYER.y = Game.WINDOW_HEIGHT - 84;
		} else if (from.equals(parent.sectors.get(2))) {
			// from sector 3
			Entities.PLAYER.x = Game.WINDOW_WIDTH - 84;
		}
	}

	@Override
	public void onPlayerLeave(MapSector to) {

	}

}
