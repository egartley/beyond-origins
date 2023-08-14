package net.egartley.beyondorigins.states;

import net.egartley.beyondorigins.Game;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenu extends BasicGameState {

    private Image logo;

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        logo = new Image("images/logo.png");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.black);
        g.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        g.drawImage(logo, 0, 0);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }

}
