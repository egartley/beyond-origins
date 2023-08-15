package net.egartley.beyondorigins.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class InGame extends BasicGameState {

    public static int ID = 1;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        // TODO: stub
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        // TODO: stub
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        // TODO: stub
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.fillRect(200, 200, 48, 48);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }

}
