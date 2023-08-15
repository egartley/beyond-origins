package net.egartley.beyondorigins;

import net.egartley.beyondorigins.engine.Mouse;
import net.egartley.beyondorigins.states.InGame;
import net.egartley.beyondorigins.states.MainMenu;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame implements InputProviderListener {

    public static final int WINDOW_WIDTH = 959;
    public static final int WINDOW_HEIGHT = 543;

    public static Mouse mouse;

    public Game() {
        super("Beyond Origins");
    }

    public static void main(String[] args) {
        mouse = new Mouse();
        try {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
            app.setTargetFrameRate(60);
            app.setShowFPS(false);
            app.setIcon("images/icon.png");
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public static void quit() {
        System.exit(0);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new MainMenu());
        this.addState(new InGame());
    }

    @Override
    public void mousePressed(int button, int x, int y) {

    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        mouse.isDragging = false;
        mouse.onClick(x, y);
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        mouse.x = newx;
        mouse.y = newy;
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        mouse.x = newx;
        mouse.y = newy;
        mouse.isDragging = true;
    }

    @Override
    public void mouseWheelMoved(int change) {

    }

    @Override
    public void keyPressed(int key, char c) {

    }

    @Override
    public void keyReleased(int key, char c) {

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
