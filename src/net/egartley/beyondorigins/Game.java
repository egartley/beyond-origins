package net.egartley.beyondorigins;

import net.egartley.beyondorigins.core.controllers.KeyboardController;
import net.egartley.beyondorigins.core.controllers.MouseController;
import net.egartley.beyondorigins.core.input.Keyboard;
import net.egartley.beyondorigins.core.input.Mouse;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.gamestates.MainMenuState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame implements InputProviderListener {

    // TODO: Look into http://slick.ninjacave.com/wiki/index.php?title=Deferred_Resource_Loading
    // TODO: Look into more entity types other than just animated
    // TODO: New game state for selecting a save file
    // TODO: New game state when pressing esc in game (pause)
    // TODO: Make quest definition files
    // TODO: Make it so that cutscene trigger doesn't need an image to work
    // TODO: Make map tile class abstract, seperate grass/sand/path etc tile classes
    // TODO: Fix/redo Util.toLines(String, Font, int)
    // TODO: Fix end of PlayerInventoryStack's tick
    // TODO: Possibly redo items, at least improve
    // TODO: Warp pad dual render and better boundary
    // TODO: Change dialogue finished events management

    public static boolean debug = true;
    public static final int WINDOW_WIDTH = 959;
    public static final int WINDOW_HEIGHT = 543;
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

    public static void quit() {
        System.exit(0);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        input = container.getInput();
        InputProvider provider = new InputProvider(input);
        provider.addListener(this);
        this.addState(new InGameState());
        this.addState(new MainMenuState());
    }

    @Override
    public void mousePressed(int button, int x, int y) {
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        Mouse.isDragging = false;
        MouseController.onMouseClick(button, x, y);
    }

    @Override
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
