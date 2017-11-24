package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Graphics;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;

public class Sector1 extends MapSector {
	
	private EntityEntityCollision playerDummyCollision;
	
	public Sector1(MapSectorDefinition def) {
		super(def);
		playerDummyCollision = new EntityEntityCollision(Entities.PLAYER.boundary, Entities.DUMMY.boundary);
	}

	@Override
	public void render(Graphics graphics) {
		drawTiles(graphics, 150, 50);
		Entities.DUMMY.render(graphics);
		Entities.PLAYER.render(graphics);
	}

	@Override
	public void tick() {
		Entities.PLAYER.tick();
		playerDummyCollision.check();
		if (playerDummyCollision.isCollided) {
			System.out.println("roblox-death-sound.mp3");
		}
	}

	@Override
	public void onEnter() {
		Entities.PLAYER.absoluteX = 100;
		Entities.PLAYER.absoluteY = 100;
	}

	@Override
	public void onExit() {
		
	}

}