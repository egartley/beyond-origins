package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Graphics;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;

public class Sector1 extends MapSector {

	private EntityEntityCollision playerDummyCollision, playerTreeCollision;

	public Sector1(MapSectorDefinition def) {
		super(def);
		playerDummyCollision = new EntityEntityCollision(Entities.PLAYER.boundary, Entities.DUMMY.boundary) {
			public void onCollision() {
				System.out.println("player & dummy collided!");
			};
		};
		playerTreeCollision = new EntityEntityCollision(Entities.PLAYER.boundary, Entities.TREE1.boundary) {
			public void onCollision() {
				System.out.println("player & tree1 collided!");
			};
		};
	}

	@Override
	public void render(Graphics graphics) {
		drawTiles(graphics, 0, 0);
		Entities.TREE1.render(graphics);
		Entities.DUMMY.render(graphics);
		Entities.PLAYER.render(graphics);
	}

	@Override
	public void tick() {
		Entities.PLAYER.tick();
		playerDummyCollision.check();
		playerTreeCollision.check();
	}

	@Override
	public void onEnter() {
		Entities.PLAYER.x = 200;
		Entities.PLAYER.y = 200;
	}

	@Override
	public void onExit() {

	}

}