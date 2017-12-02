package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Tree1;
import net.egartley.beyondorigins.logic.collision.Collision;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;
import net.egartley.beyondorigins.objects.Sprite;

public class Sector1 extends MapSector {

	private EntityEntityCollision playerDummyCollision, playerTree1Collision, playerTree2Collision;
	private ArrayList<Tree1> trees;
	private ArrayList<Collision> collisions;

	public Sector1(MapSectorDefinition def) {
		super(def);

		// sector-specific entities
		trees = new ArrayList<Tree1>();
		Sprite s = Entities.TREE1.currentSprite;
		trees.add(new Tree1(s, 100, 200));
		trees.add(new Tree1(s, 300, 400));

		// sector-specific collisions
		playerDummyCollision = new EntityEntityCollision(Entities.PLAYER.boundary, Entities.DUMMY.boundary) {
			public void onCollision() {

			};
		};
		playerTree1Collision = new EntityEntityCollision(Entities.PLAYER.boundary, trees.get(0).boundary) {
			public void onCollision() {
				Entities.TREE1.playerCollision(entity2.boundary);
			};

			public void postCollision() {
				Entities.PLAYER.enableAllMovement();
			};
		};
		playerTree2Collision = new EntityEntityCollision(Entities.PLAYER.boundary, trees.get(1).boundary) {
			public void onCollision() {
				Entities.TREE1.playerCollision(entity2.boundary);
			};

			public void postCollision() {
				Entities.PLAYER.enableAllMovement();
			};
		};
		collisions = new ArrayList<Collision>();
		collisions.add(playerDummyCollision);
		collisions.add(playerTree1Collision);
		collisions.add(playerTree2Collision);
	}

	@Override
	public void render(Graphics graphics) {
		drawTiles(graphics, 0, 0);
		for (Tree1 tree : trees) {
			tree.render(graphics);
		}
		Entities.DUMMY.render(graphics);
		Entities.PLAYER.render(graphics);
	}

	@Override
	public void tick() {
		Entities.PLAYER.tick();
		for (Collision c : collisions) {
			c.tick();
		}
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