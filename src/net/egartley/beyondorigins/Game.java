package net.egartley.beyondorigins;

import net.egartley.beyondorigins.controllers.KeyboardController;
import net.egartley.beyondorigins.definitions.dialogue.DummyDialogue;
import net.egartley.beyondorigins.definitions.maps.AllSectors;
import net.egartley.beyondorigins.entities.Dummy;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Player;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.gamestates.MainMenuState;
import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.gamelib.graphics.SpriteSheet;
import net.egartley.gamelib.input.KeyTyped;
import net.egartley.gamelib.input.Keyboard;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.objects.GameState;
import net.egartley.gamelib.objects.MapTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

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
    public static final int WINDOW_WIDTH = windowDimension.width - 17;
    public static final int WINDOW_HEIGHT = windowDimension.height - 40;

    // THREADS
    private static Thread mainThread;

    // FLAGS
    /**
     * Whether or not to perform debug related operations
     */
    public static boolean debug = true;

    // GAME STATES
    public static GameState currentGameState;
    public static InGameState inGameState;
    public static MainMenuState mainMenuState;

    private void init() {
        Debug.out("Initializing graphics and entities...");
        initializeEntities();
        Debug.out("Graphics and entities were initialized");

        Debug.out("Defining dialogue...");
        DummyDialogue.initialize();
        Debug.out("Dialogue was defined");

        Debug.out("Loading maps...");
        loadMaps();
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

        currentGameState = mainMenuState;

        Debug.out("Game states were initialized");
    }

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

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

    private void initializeEntities() {
        // this up-scales images to a factor of 2 (each pixel in the source image will be rendered as 2x2 pixel)
        byte scale = 2;

        // *********** PLAYER BEGIN ***********
        BufferedImage image = ImageStore.get(ImageStore.PLAYER);
        if (image != null) {
            image = Util.resize(image, image.getWidth(), image.getHeight());
        } else {
            Debug.error("The default player image (\"player-default.png\") doesn't exist, or there was a problem while loading it!");
            return;
        }
        Entities.PLAYER = new Player(new SpriteSheet(image, 30, 46, 2, 4).sprites);
        // ************ PLAYER END ************

        // ************ DUMMY BEGIN ***********
        image = ImageStore.get(ImageStore.DUMMY);
        if (image != null) {
            image = Util.resize(image, image.getWidth() * scale, image.getHeight() * scale);
        } else {
            Debug.error("The dummy image (\"dummy.png\") doesn't exist, or there was a problem while loading it!");
            return;
        }
        Entities.DUMMY = new Dummy(new SpriteSheet(image, 15 * scale, 23 * scale, 2, 4).sprites);
        // ************ DUMMY END *************
    }

    /**
     * Loads all maps and their sectors' tile definitions
     */
    private void loadMaps() {
        MapTile.init();
        AllSectors.define();
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

        // Credit: http://www.java-gaming.org/index.php?topic=24220.0
        // Additional credit: http://www.cokeandcode.com/info/showsrc/showsrc.php?src=../spaceinvaders102/org/newdawn/spaceinvaders/Game.java
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) OPTIMAL_TIME);
            lastFpsTime += updateLength;
            fps++;
            if (lastFpsTime >= 1000000000) {
                lastFpsTime = 0;
                fps = 0;
            }

            tick(delta);
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
        currentGameState.tick();
    }

    private synchronized void render(Graphics graphics) {
        if (currentGameState == null) {
            return;
        }
        currentGameState.render(graphics);
    }

    @Override
    public void paint(Graphics graphics) {
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        render(graphics);
    }

}
