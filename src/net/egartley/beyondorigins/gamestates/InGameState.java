package net.egartley.beyondorigins.gamestates;

import java.awt.Graphics;

import net.egartley.beyondorigins.maps.testmap.TestMap;
import net.egartley.beyondorigins.objects.GameState;
import net.egartley.beyondorigins.objects.Map;

public class InGameState extends GameState {

	public Map currentMap;

	public InGameState() {
		identificationNumber = GameState.IN_GAME;
		currentMap = new TestMap();
	}

	@Override
	public void render(Graphics graphics)
	{
		currentMap.render(graphics);
	}

	@Override
	public void tick()
	{
		currentMap.tick();
	}

}
