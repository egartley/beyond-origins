package net.egartley.beyondorigins;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import net.egartley.beyondorigins.definitions.maps.AllSectors;
import net.egartley.beyondorigins.entities.*;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.maps.TileBuilder;
import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.beyondorigins.objects.GameState;
import net.egartley.beyondorigins.objects.SpriteSheet;
import net.egartley.beyondorigins.threads.MasterTick;

public class Game extends Canvas implements Runnable {

	// SELF
	private static final long serialVersionUID = 8213282993283826186L;
	private static short frames, currentFrames;
	private static JFrame frame;
	private static Dimension windowDimension = new Dimension(998, 573);
	private Graphics graphics;
	private BufferStrategy bufferStrategy;

	// CONSTANTS
	public static final int WINDOW_WIDTH = windowDimension.width - 7, WINDOW_HEIGHT = windowDimension.height - 30;

	// THREADS
	private static Thread masterRenderThread;
	private static Thread masterTickThread;

	// THREAD OBJECTS
	private static MasterTick tick = new MasterTick();

	// FLAGS
	public static boolean running = false;
	public static boolean runTickThread = true;
	public static boolean debug = true;

	// GAMESTATES
	public static GameState currentGameState;

	private void init() {
		loadGraphicsAndEntities();
		loadMaps();
		currentGameState = new InGameState();
		this.addKeyListener(new Keyboard());
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.setPreferredSize(windowDimension);
		game.setMaximumSize(windowDimension);
		game.setMinimumSize(windowDimension);
		// initialize jframe with title
		frame = new JFrame("Beyond Origins");
		// set window size
		frame.setSize(windowDimension.width, windowDimension.height);
		// normal close operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// enforce window size (for now)
		frame.setResizable(false);
		// "add" the game to the frame so things will actually display and update
		frame.add(game);
		// center the frame's window in the user's screen
		frame.setLocationRelativeTo(null);
		// actually show the frame
		frame.setVisible(true);
		// actually start the game
		game.start();
	}

	private void loadGraphicsAndEntities() {
		// this loads all of the images in "resources/images"
		ImageStore.loadAll();
		// this upscales all images to a factor of 2 (i.e. each pixel in an image will
		// be rendered as 2x2 pixels)
		byte scale = 2;

		// *********** PLAYER BEGIN ***********
		BufferedImage image = ImageStore.playerDefault;
		if (image != null) {
			// upscale the image by a factor of 2 (double it)
			image = Util.resized(image, image.getWidth() * scale, image.getHeight() * scale);
		} else {
			Debug.error(
					"The default player image (\"player-default.png\") doesn't exist, or there was a problem while loading it!");
			return;
		}
		// initialize the player
		Entities.PLAYER = new Player(
				new SpriteSheet(image, 15 * scale, 23 * scale, 2, (short) 4).getSpriteCollection());
		// ************ PLAYER END ************

		// ************ DUMMY BEGIN ***********
		image = ImageStore.dummy;
		if (image != null) {
			// upscale the image by a factor of 2 (double it)
			image = Util.resized(image, image.getWidth() * scale, image.getHeight() * scale);
		} else {
			Debug.error("The dummy image (\"dummy.png\") doesn't exist, or there was a problem while loading it!");
			return;
		}
		// initialize the dummy
		Entities.DUMMY = new Dummy(
				new SpriteSheet(image, 15 * scale, 23 * scale, 2, (short) 4).getSpriteCollection().get(0));
		// ************ DUMMY END *************

		// ******** DEFAULT TREE BEGIN ********
		// initialize the default tree
		Entities.TREE = new DefaultTree(new SpriteSheet(ImageStore.treeDefault, ImageStore.treeDefault.getWidth(),
				ImageStore.treeDefault.getHeight(), 1, (short) 1).getSpriteCollection().get(0));
		// ******** DEFAULT TREE END **********

		// ******** DEFAULT ROCK BEGIN ********
		// initialize the default rock
		Entities.ROCK = new DefaultRock(new SpriteSheet(ImageStore.rockDefault, ImageStore.rockDefault.getWidth(),
				ImageStore.rockDefault.getHeight(), 1, (short) 1).getSpriteCollection().get(0));
		// ******** DEFAULT TREE END **********
	}

	/**
	 * Loads all maps and their sectors' tile definitions
	 */
	private void loadMaps() {
		// load map tiles used while rendering individual sectors
		TileBuilder.load();
		// define all of the map sectors, which is basically just their tile layout
		AllSectors.define();
	}

	private synchronized void start() {
		if (running == true) {
			// already "running" so the render and tick threads should have already been sta
			return;
		}
		running = true;
		masterRenderThread = new Thread(this);
		masterRenderThread.setPriority(1);
		masterRenderThread.setName("Main-Render");

		// this actually starts the tick thread for the first time
		restartMainTickThread();
		// starts the rendering thread
		masterRenderThread.start();
	}

	private synchronized void stop() {
		if (running == false) {
			return;
		}
		// stops the fps system, thus ending calls to render and tick methods
		running = false;
		try {
			masterRenderThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// load images, save data, etc.
		init();
		// enable double buffering
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
		// get graphics object to render to
		graphics = bufferStrategy.getDrawGraphics();
		// enable anti-aliasing for strings
		Graphics2D g2d = (Graphics2D) graphics;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// actually render
		render();
		// setup system for ensuring that the game runs at most 60 fps
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 16666666.666666666;
		double delta = 0.0D;
		// request user's operating system "focus", i.e. mouse and keyboard input
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0D) {
				// render with the graphics object (tick is in a seperate thread)
				render();
				delta -= 1.0D;
				frames += 1;
				if (System.currentTimeMillis() - timer > 1000L) {
					timer += 1000L;
					currentFrames = frames;
					frames = 0;
				}
			}
			// this helps to stabilize the fps system
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// end the game, terminate application process
		stop();
	}

	private synchronized void render() {
		// ********** RENDER BEGIN ***********

		currentGameState.render(graphics);

		// *********** RENDER END ************

		bufferStrategy.show();
	}

	/**
	 * Stops all calls to tick methods, but render methods are still called
	 */
	public static void stopMainTickThread() {
		runTickThread = false;
	}

	/**
	 * Restarts the main tick thread, which enables calls to tick methods
	 */
	public static void restartMainTickThread() {
		runTickThread = true;
		masterTickThread = new Thread(tick);
		masterTickThread.setPriority(2);
		masterTickThread.setName("Main-Tick");
		masterTickThread.start();
	}

	/**
	 * Returns the current FPS, which should always be between 60 and 57
	 * 
	 * @return The game's current frames per second
	 */
	public static short getFramesPerSecond() {
		return currentFrames;
	}

}
