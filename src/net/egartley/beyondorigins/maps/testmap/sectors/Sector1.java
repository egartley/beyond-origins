package net.egartley.beyondorigins.maps.testmap.sectors;

import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.DefaultTree;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;
import net.egartley.beyondorigins.objects.Sprite;

public class Sector1 extends MapSector {

	private ArrayList<DefaultTree>		trees;

	public Sector1(MapSectorDefinition def) {
		super(def);

		// sector-specific entities
		trees = new ArrayList<DefaultTree>();
		Sprite s = Entities.TREE.sprite;
		trees.add(new DefaultTree(s, 100, 200));
		trees.add(new DefaultTree(s, 36, 200));
	}

	@Override
	public void render(Graphics graphics)
	{
		drawTiles(graphics, 0, 0);
		Entities.DUMMY.render(graphics);
		Entities.PLAYER.render(graphics);
		for (DefaultTree tree : trees) {
			tree.render(graphics);
		}
	}

	@Override
	public void tick()
	{
		Entities.PLAYER.tick();
		for (DefaultTree tree : trees) {
			tree.tick();
		}
	}

	@Override
	public void onEnter()
	{
		Entities.PLAYER.x = 200;
		Entities.PLAYER.y = 200;
	}

	@Override
	public void onExit()
	{

	}

}
