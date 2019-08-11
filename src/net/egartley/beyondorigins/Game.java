package net.egartley.beyondorigins;

import net.egartley.beyondorigins.controllers.KeyboardController;
import net.egartley.beyondorigins.definitions.maps.AllSectors;
import net.egartley.beyondorigins.entities.Dummy;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Player;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.gamestates.MainMenuState;
import net.egartley.gamelib.input.KeyTyped;
import net.egartley.gamelib.input.Keyboard;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.objects.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author Evan Gartley (https://github.com/egartley)
 * @version https://egartley.net/projects/beyond-origins/?via=javadocgameclass
 */
public class Game extends JPanel implements Runnable {

    // SELF
    private static final long serialVersionUID = 8213282993283826186L;
    private static long startTime;
    private static JFrame frame;
    private static Dimension windowDimension = new Dimension(976, 583);
    private static boolean running = false;
    private int fps;
    private long lastFpsTime;

    // CONSTANTS
    /**
     * The "actual" width of the window
     */
    public static final int WINDOW_WIDTH = windowDimension.width - 17;
    /**
     * The "actual" height of the window
     */
    public static final int WINDOW_HEIGHT = windowDimension.height - 40;

    // THREADS
    private static Thread mainThread;

    // FLAGS
    /**
     * Whether or not to perform debug related operations
     */
    public static boolean debug = true;

    // GAME STATES
    private static InGameState inGameState;
    private static MainMenuState mainMenuState;

    public static GameState currentState;

    public static void main(String[] args) {
        startTime = System.nanoTime();

        frame = new JFrame("Beyond Origins");
        frame.setSize(windowDimension.width, windowDimension.height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Credit: https://stackoverflow.com/a/11671311
        Game game = new Game();
        game.setDoubleBuffered(true);
        frame.getContentPane().add(game);

        frame.setVisible(true);
        game.requestFocus();

        running = true;
        mainThread = new Thread(game, "Game-Main");
        mainThread.start();
    }

    private void init() {
        Debug.out("Initializing entities...");
        initializeEntities();
        Debug.out("Entities were initialized");

        Debug.out("Loading maps...");
        AllSectors.define();
        Debug.out("Maps were loaded");

        Debug.out("Initializing input stuff...");
        this.addKeyListener(new Keyboard());
        Mouse m = new Mouse();
        this.addMouseListener(m);
        this.addMouseMotionListener(m);
        KeyboardController.addKeyTyped(new KeyTyped(KeyEvent.VK_F3) {
            @Override
            public void onType() {
                debug = !debug;
            }
        });
        Debug.out("Input stuff was initialized");

        Debug.out("Initializing game states...");
        inGameState = new InGameState();
        mainMenuState = new MainMenuState();
        setState(mainMenuState);
        Debug.out("Game states were initialized");
    }

    private void initializeEntities() {
        Entities.PLAYER = new Player();
        Entities.DUMMY = new Dummy();
    }

    public static boolean isState(int id) {
        switch (id) {
            case GameState.IN_GAME:
                return inGameState.equals(currentState);
            case GameState.MAIN_MENU:
                return mainMenuState.equals(currentState);
            default:
                Debug.out("Tried to get an unknown game state (id of " + id + ")");
                return false;
        }
    }

    public static GameState getState(int id) {
        switch (id) {
            case GameState.IN_GAME:
                return inGameState;
            case GameState.MAIN_MENU:
                return mainMenuState;
            default:
                Debug.out("Tried to get an unknown game state (id of " + id + ")");
                return currentState;
        }
    }

    public static InGameState in() {
        return (InGameState) getState(GameState.IN_GAME);
    }

    public static void setState(int id) {
        setState(getState(id));
    }

    public static void setState(GameState changeTo) {
        if (changeTo.equals(currentState)) {
            Debug.warning("Tried to change the game state to the state it's already in");
            return;
        }
        if (currentState != null) {
            currentState.onEnd();
        }
        currentState = changeTo;
        currentState.onStart();
    }

    public static synchronized void quit() {
        System.exit(0);
    }

    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            mainThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // load images, save data, etc.
        init();
        Debug.out("init() took " + ((System.nanoTime() - startTime) / 1000000000.0) + " seconds");

        // Credit:
        // http://www.java-gaming.org/index.php?topic=24220.0
        // http://www.cokeandcode.com/info/showsrc/showsrc.php?src=../spaceinvaders102/org/newdawn/spaceinvaders/Game.java
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            lastFpsTime += updateLength;
            fps++;
            if (lastFpsTime >= 1000000000) {
                lastFpsTime = 0;
                fps = 0;
            }

            tick(updateLength / (double) OPTIMAL_TIME);
            repaint();

            try {
                Thread.sleep(Math.abs((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stop();
    }

    private synchronized void tick(double delta) {
        // ignore interpolation for now
        currentState.tick();
    }

    private synchronized void render(Graphics graphics) {
        if (currentState == null) {
            return;
        }
        currentState.render(graphics);
    }

    @Override
    public void paint(Graphics graphics) {
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        render(graphics);
    }

}
