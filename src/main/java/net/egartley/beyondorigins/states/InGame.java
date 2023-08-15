package net.egartley.beyondorigins.states;

import net.egartley.beyondorigins.engine.LevelMap;
import net.egartley.beyondorigins.levelmaps.debug.DebugMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class InGame extends BasicGameState {

    public static int ID = 1;

    private LevelMap currentMap;
    private LevelMap[] levelMaps;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        levelMaps = new LevelMap[1];
        levelMaps[0] = new DebugMap();
        currentMap = levelMaps[0];
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        // TODO: stub
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        currentMap.currentSector.render(g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }

}
