package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.core.ui.MenuButton;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class MainMenuState extends BasicGameState {

    private final Image logo;
    private final Color backgroundColor = Color.lightGray;

    public static final int ID = 0;
    public ArrayList<MenuButton> buttons = new ArrayList<>();

    public MainMenuState() {
        logo = Images.get("resources/images/logo.png");
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        buttons.add(new MenuButton("Resume", true, (Game.WINDOW_WIDTH / 2) - 47, (Game.WINDOW_HEIGHT / 2) - 16, 94, 32) {
            @Override
            public void onClick() {
                game.enterState(InGameState.ID);
            }
        });
        buttons.add(new MenuButton("New Game", true, (Game.WINDOW_WIDTH / 2) - 60, (Game.WINDOW_HEIGHT / 2) - 16 + 52, 120, 32) {
            @Override
            public void onClick() {

            }
        });
        buttons.add(new MenuButton("Quit", true, (Game.WINDOW_WIDTH / 2) - 28, (Game.WINDOW_HEIGHT / 2) - 16 + 104, 56, 32) {
            @Override
            public void onClick() {
                Game.quit();
            }
        });
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        buttons.forEach(MenuButton::registerClicked);
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        buttons.forEach(MenuButton::deregisterClicked);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        graphics.drawImage(logo, Calculate.getCenter(Game.WINDOW_WIDTH / 2, logo.getWidth()), 130);
        buttons.forEach(b -> b.render(graphics));
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        buttons.forEach(MenuButton::tick);
    }

    @Override
    public int getID() {
        return MainMenuState.ID;
    }

}
