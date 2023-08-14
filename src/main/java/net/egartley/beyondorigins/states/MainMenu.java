package net.egartley.beyondorigins.states;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.engine.TextButton;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenu extends BasicGameState {

    public static int ID = 0;

    private Image logo;
    private TextButton[] buttons;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        logo = new Image("images/logo.png");
        buttons = new TextButton[3];
        buttons[0] = new TextButton(Game.WINDOW_WIDTH / 2 - 75, 260, 150, 32, "Resume");
        buttons[1] = new TextButton(Game.WINDOW_WIDTH / 2 - 75, 260 + 48, 150, 32, "New Game");
        buttons[2] = new TextButton(Game.WINDOW_WIDTH / 2 - 75, 260 + 96, 150, 32, "Quit");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        g.drawImage(logo, Game.WINDOW_WIDTH / 2 - logo.getWidth() / 2, 128);
        for (TextButton tb : buttons) tb.render(g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        for (TextButton tb : buttons) tb.tick();
    }

}
