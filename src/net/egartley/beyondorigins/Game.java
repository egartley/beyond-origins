package net.egartley.beyondorigins;

import net.egartley.beyondorigins.controllers.KeyboardController;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.gamestates.MainMenuState;
import net.egartley.gamelib.input.Keyboard;
import net.egartley.gamelib.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame implements InputProviderListener {

    /**
     * The "actual" width of the window
     */
    public static final int WINDOW_WIDTH = 959;
    /**
     * The "actual" height of the window
     */
    public static final int WINDOW_HEIGHT = 543;

    /**
     * Whether or not to perform debug related operations
     */
    public static boolean debug = true;

    public static Input input;

    public Game() {
        super("Beyond Origins");
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
            app.setTargetFrameRate(60);
            app.setShowFPS(false);
            app.setIcon("resources/images/icon.png");
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        input = container.getInput();

        InputProvider provider = new InputProvider(input);
        provider.addListener(this);

        Entities.initialize();
        this.addState(new InGameState());
        this.addState(new MainMenuState());
    }

    public static void quit() {
        System.exit(0);
    }

    public void mousePressed(int button, int x, int y) {

    }

    public void mouseReleased(int button, int x, int y) {
        Mouse.isDragging = false;
    }

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        Mouse.x = newx;
        Mouse.y = newy;
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        Mouse.x = newx;
        Mouse.y = newy;
        Mouse.isDragging = true;
    }

    @Override
    public void mouseWheelMoved(int change) {

    }

    @Override
    public void keyPressed(int key, char c) {
        Keyboard.keyPressed(key);
    }

    @Override
    public void keyReleased(int key, char c) {
        KeyboardController.onKeyTyped(key);
        Keyboard.keyReleased(key);
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {

    }

    @Override
    public void controlPressed(Command command) {

    }

    @Override
    public void controlReleased(Command command) {

    }

}
