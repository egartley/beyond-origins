package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.DefaultRock;
import net.egartley.beyondorigins.entities.DefaultTree;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.logic.collision.MapSectorChangeAreaCollision;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeArea;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;
import net.egartley.beyondorigins.objects.Sprite;

public class Sector1 extends MapSector {

	private ArrayList<DefaultTree> trees;
	private ArrayList<DefaultRock> rocks;

	public Sector1(Map parent, MapSectorDefinition def) {
		super(parent, def, new MapSectorChangeArea(Game.WINDOW_WIDTH / 2 - 37, 50, 74, 74));
	}

	@Override
	public void render(Graphics graphics) {
		drawTiles(graphics);

		for (DefaultTree tree : trees) {
			tree.drawFirstLayer(graphics);
		}
		for (DefaultRock rock : rocks) {
			rock.drawFirstLayer(graphics);
		}

		Entities.DUMMY.render(graphics);
		Entities.PLAYER.render(graphics);

		for (DefaultTree tree : trees) {
			tree.drawSecondLayer(graphics);
		}
		for (DefaultRock rock : rocks) {
			rock.drawSecondLayer(graphics);
		}

		for (MapSectorChangeArea area : changeAreas) {
			area.draw(graphics);
		}
	}

	@Override
	public void tick() {
		Entities.PLAYER.tick();
		for (DefaultTree tree : trees) {
			tree.tick();
		}
		for (DefaultRock rock : rocks) {
			rock.tick();
		}
		for (MapSectorChangeAreaCollision changeAreaCollision : changeAreaCollisions) {
			changeAreaCollision.tick();
		}
	}

	@Override
	public void onPlayerEnter() {
		// set default/initial position
		Entities.PLAYER.x = 200;
		Entities.PLAYER.y = 200;

		// sector-specific entities
		trees = new ArrayList<DefaultTree>();
		Sprite s = Entities.TREE.sprite;
		trees.add(new DefaultTree(s, 100, 200));
		trees.add(new DefaultTree(s, 36, 200));

		rocks = new ArrayList<DefaultRock>();
		// re-use same sprite variable, no use in creating a new one if there is already
		// one in memory
		s = Entities.ROCK.sprite;
		rocks.add(new DefaultRock(s, 300, 160));
		rocks.add(new DefaultRock(s, 270, 310));
		rocks.add(new DefaultRock(s, 150, 370));
		rocks.add(new DefaultRock(s, 460, 350));
		rocks.add(new DefaultRock(s, 200, 115));
	}

	@Override
	public void onPlayerLeave() {
		// de-register all sector-specific entities
		for (DefaultTree tree : trees) {
			tree.kill();
		}
		for (DefaultRock rock : rocks) {
			rock.kill();
		}
	}

}
