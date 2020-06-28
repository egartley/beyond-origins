package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.ui.MenuButton;
import net.egartley.gamelib.logic.math.Calculate;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class MainMenuState extends BasicGameState {

    public static final int ID = 0;

    public ArrayList<MenuButton> buttons = new ArrayList<>();

    public MainMenuState(GameContainer container, StateBasedGame game) {
        try {
            init(container, game);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        buttons.add(new MenuButton("Play", true, (Game.WINDOW_WIDTH / 2) - 50, (Game.WINDOW_HEIGHT / 2) - 16, 100, 32) {
            @Override
            public void onClick() {
                game.enterState(InGameState.ID);
            }
        });
        buttons.add(new MenuButton("Quit", true, (Game.WINDOW_WIDTH / 2) - 50, (Game.WINDOW_HEIGHT / 2) - 16 + 52, 100, 32) {
            @Override
            public void onClick() {
                Game.quit();
            }
        });
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
        // background
        graphics.setColor(Color.lightGray);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);

        // logo placeholder
        graphics.setColor(Color.black);
        graphics.drawRect(Calculate.getCenter(Game.WINDOW_WIDTH / 2, 300), 52, 300, 150);

        for (MenuButton b : buttons) {
            b.render(graphics);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        for (MenuButton b : buttons) {
            b.tick();
        }
    }

    @Override
    public int getID() {
        return MainMenuState.ID;
    }

}
