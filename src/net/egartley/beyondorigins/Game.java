package net.egartley.beyondorigins;

import net.egartley.beyondorigins.definitions.maps.AllSectors;
import net.egartley.beyondorigins.entities.Dummy;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Player;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.gamestates.MainMenuState;
import net.egartley.beyondorigins.graphics.EntityExpression;
import net.egartley.beyondorigins.ingame.DialoguePanel;
import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.input.Mouse;
import net.egartley.beyondorigins.maps.TileBuilder;
import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.beyondorigins.objects.GameState;
import net.egartley.beyondorigins.objects.SpriteSheet;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * @author Evan Gartley
 */
public class Game extends Canvas implements Runnable {

    // SELF
    private static final long serialVersionUID = 8213282993283826186L;
    private static long startTime;
    private static short frames;
    private static JFrame frame;
    private static Dimension windowDimension = new Dimension(998, 573);
    private static boolean running = false;
    private Graphics graphics;
    private BufferStrategy bufferStrategy;

    // CONSTANTS
    public static final int WINDOW_WIDTH = windowDimension.width - 7;
    public static final int WINDOW_HEIGHT = windowDimension.height - 30;

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
        loadGraphicsAndEntities();
        Debug.out("Graphics and entities were initialized");
        Debug.out("Loading maps...");
        loadMaps();
        Debug.out("Maps were loaded");

        inGameState = new InGameState();
        mainMenuState = new MainMenuState();
        if (debug) {
            currentGameState = inGameState;
        } else {
            currentGameState = mainMenuState;
        }

        this.addKeyListener(new Keyboard());
        Mouse m = new Mouse();
        this.addMouseListener(m);
        this.addMouseMotionListener(m);
    }

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        Game game = new Game();
        frame = new JFrame("Beyond Origins");
        frame.setSize(windowDimension.width, windowDimension.height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // enforce window size (for now)
        frame.setResizable(false);
        frame.add(game);
        // center the frame's window in the user's screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Debug.out("Initialized the JFrame");
        game.start();
    }

    /**
     * Loads all of the images in "/resources/images"
     */
    private void loadGraphicsAndEntities() {
        Debug.out("Loading images...");
        ImageStore.loadAll();

        // ************ EXPRESSIONS BEGIN *****
        EntityExpression.init();
        // ************ EXPRESSIONS END *******

        // this up-scales all images to a factor of 2 (each pixel in the source image will be rendered as 2x2 pixel)
        byte scale = 2;

        // *********** PLAYER BEGIN ***********
        BufferedImage image = ImageStore.playerDefault;
        if (image != null) {
            image = Util.resize(image, image.getWidth() * scale, image.getHeight() * scale);
        } else {
            Debug.error(
                    "The default player image (\"player-default.png\") doesn't exist, or there was a problem while " +
                            "loading it!");
            return;
        }
        Entities.PLAYER = new Player(new SpriteSheet(image, 15 * scale, 23 * scale, 2, 4).sprites);
        // ************ PLAYER END ************
        Debug.out("Initialized the player");

        // ************ DUMMY BEGIN ***********
        image = ImageStore.dummy;
        if (image != null) {
            image = Util.resize(image, image.getWidth() * scale, image.getHeight() * scale);
        } else {
            Debug.error("The dummy image (\"dummy.png\") doesn't exist, or there was a problem while loading it!");
            return;
        }
        Entities.DUMMY = new Dummy(new SpriteSheet(image, 15 * scale, 23 * scale, 2, 4).sprites);
        // ************ DUMMY END *************
        Debug.out("Initialized the dummy");

        Entities.DIALOGUE_PANEL = new DialoguePanel(Entities.getSpriteTemplate(Entities.DIALOGUE));
    }

    /**
     * Loads all maps and their sectors' tile definitions
     */
    private void loadMaps() {
        Debug.out("Loading tiles...");
        TileBuilder.load();
        Debug.out("Defining sectors...");
        AllSectors.define();
    }

    private synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        mainThread = new Thread(this);
        mainThread.setPriority(1);
        mainThread.setName("GameThread");
        mainThread.start();
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
        Debug.out("Starting main game thread...");
        // load images, save data, etc.
        init();
        // double buffering
        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();
        // enable anti-aliasing for strings
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // setup system for ensuring that the game runs at most 60 fps
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double ns = 16666666.666666666;
        double delta = 0.0D;
        requestFocus();
        Debug.out("Startup: " + ((System.currentTimeMillis() - startTime) / 1000.0) + " seconds");
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0D) {
                tick();
                render();
                delta -= 1.0D;
                frames += 1;
                if (System.currentTimeMillis() - timer > 1000L) {
                    timer += 1000L;
                    frames = 0;
                }
            }
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    private synchronized void tick() {
        currentGameState.tick();
    }

    private synchronized void render() {
        currentGameState.render(graphics);
        bufferStrategy.show();
    }

}
