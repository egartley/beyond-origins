package net.egartley.beyondorigins;

import net.egartley.beyondorigins.controllers.KeyboardController;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.MainMenuState;
import net.egartley.beyondorigins.gamestates.ingame.InGameState;
import net.egartley.gamelib.abstracts.GameState;
import net.egartley.gamelib.input.KeyTyped;
import net.egartley.gamelib.input.Keyboard;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.logic.math.Calculate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Game extends Canvas implements Runnable {

    // SELF
    private static final long serialVersionUID = 8213282993283826186L;
    private static long startTime;
    private static JFrame frame;
    private static String loadingStatus = "Loading...";
    private static final Dimension windowDimension = new Dimension(976, 583);
    private static boolean running = false, started = false;
    private long lastFpsTime;
    private int fps;

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

        Game game = new Game();
        frame.getContentPane().add(game);
        frame.setVisible(true);
        game.setFocusable(true);
        game.requestFocusInWindow();

        running = true;
        mainThread = new Thread(game, "Game-Main");
        mainThread.start();
    }

    private void init() {
        Debug.out("Initializing input...");
        setLoadingStatus("Loading... (input)");
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
        Debug.out("Input was initialized");

        Debug.out("Initializing entities...");
        setLoadingStatus("Loading... (entities)");
        Entities.initialize();
        Debug.out("Entities were initialized");

        Debug.out("Initializing game states...");
        setLoadingStatus("Loading... (ingame state)");
        inGameState = new InGameState();
        setLoadingStatus("Loading... (mainmenu state)");
        mainMenuState = new MainMenuState();
        if (debug) {
            setState(inGameState);
        } else {
            setState(mainMenuState);
        }
        Debug.out("Game states were initialized");
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

    public static void setLoadingStatus(String status) {
        loadingStatus = status;
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
        boolean success = false;
        try {
            Debug.out("Starting initialization...");
            init();
            success = true;
        } catch (Exception e) {
            Debug.out("FATAL ERROR: Did not successfully initialize!");
            e.printStackTrace();
        }

        if (!success) {
            quit();
        } else {
            Debug.out("Initialization took " + ((System.nanoTime() - startTime) / 1000000000.0) + " seconds");
            started = true;

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

                try {
                    Thread.sleep(Math.abs((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            stop();
        }
    }

    @Override
    public void update(Graphics graphics) {
        // Credit: https://web.archive.org/web/20101020140036/http://home.comcast.net/~jml3on/java/tricks/dbuf.java
        Graphics offgc;
        Image offscreen = null;
        Rectangle box = graphics.getClipBounds();
        // create the offscreen buffer and associated Graphics
        offscreen = createImage(box.width, box.height);
        offgc = offscreen.getGraphics();
        // clear the exposed area
        offgc.setColor(getBackground());
        offgc.fillRect(0, 0, box.width, box.height);
        offgc.setColor(getForeground());
        // do normal redraw
        offgc.translate(-box.x, -box.y);
        paint(offgc);
        // transfer offscreen to window
        graphics.drawImage(offscreen, box.x, box.y, this);
    }

    private void tick(double delta) {
        // ignore interpolation for now
        currentState.tick();
    }

    private void render(Graphics graphics) {
        if (currentState == null) {
            return;
        }
        currentState.render(graphics);
    }

    @Override
    public void paint(Graphics graphics) {
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (started) {
            render(graphics);
        } else {
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Consolas", Font.PLAIN, 32));
            graphics.drawString(loadingStatus, Calculate.getCenteredX(graphics.getFontMetrics().stringWidth(loadingStatus)), Calculate.getCenteredY(32) + 16);
        }
        repaint();
    }

}
