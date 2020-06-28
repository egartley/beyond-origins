package net.egartley.beyondorigins;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.MainMenuState;
import net.egartley.beyondorigins.gamestates.ingame.InGameState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

    // CONSTANTS
    /**
     * The "actual" width of the window
     */
    public static final int WINDOW_WIDTH = 959;
    /**
     * The "actual" height of the window
     */
    public static final int WINDOW_HEIGHT = 543;

    // FLAGS
    /**
     * Whether or not to perform debug related operations
     */
    public static boolean debug = true;

    public Game() {
        super("Beyond Origins");
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
            app.setTargetFrameRate(60);
            app.setShowFPS(false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        Entities.initialize();
        this.addState(new InGameState(container, this));
        this.addState(new MainMenuState(container, this));
    }

    public static void quit() {
        System.exit(0);
    }

}
