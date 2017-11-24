package net.egartley.beyondorigins;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import net.egartley.beyondorigins.entities.Dummy;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Player;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.input.Keyboard;
import net.egartley.beyondorigins.maps.TileBuilder;
import net.egartley.beyondorigins.media.images.ImageStore;
import net.egartley.beyondorigins.objects.GameState;
import net.egartley.beyondorigins.objects.SpriteSheet;
import net.egartley.beyondorigins.threads.MainTick;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 8213282993283826186L;
	private static short frames, currentFrames;
	private static JFrame frame;
	private static Dimension windowDimension = new Dimension(1024, 576);
	private Graphics graphics;

	private static Thread renderThread;
	private static Thread tickThread;

	public static boolean running = false;
	public static boolean runTickThread = true;
	public static boolean drawBoundaries = true;

	private static MainTick tick = new MainTick();

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
		frame = new JFrame("Beyond Origins");
		frame.setSize(windowDimension.width, windowDimension.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.start();
	}

	private void loadGraphicsAndEntities() {
		ImageStore.loadAll();
		byte scale = 2;
		
		// *********** PLAYER BEGIN ***********
		BufferedImage playerImage = ImageStore.playerDefault;
		if (playerImage != null) {
			playerImage = Util.resized(playerImage, playerImage.getWidth() * scale, playerImage.getHeight() * scale);
		}
		Entities.PLAYER = new Player(new SpriteSheet(playerImage, 15 * scale, 23 * scale, 2, 4).getSpriteCollection());
		// ************ PLAYER END ************
		
		// ************ DUMMY BEGIN ***********
		BufferedImage dummyImage = ImageStore.dummy;
		if (dummyImage != null) {
			dummyImage = Util.resized(dummyImage, dummyImage.getWidth() * scale, dummyImage.getHeight() * scale);
		}
		Entities.DUMMY = new Dummy(new SpriteSheet(dummyImage, 15 * scale, 23 * scale, 2, 4).getSpriteCollection().get(0));
		// ************ DUMMY END *************
	}

	private void loadMaps() {
		TileBuilder.load();
		net.egartley.beyondorigins.definitions.maps.testmap.Sectors.defineAll();
	}

	private synchronized void start() {
		if (running) {
			return;
		}
		running = true;

		renderThread = new Thread(this);

		renderThread.setPriority(1);

		renderThread.setName("Main-Render");

		restartMainTickThread();
		renderThread.start();
	}

	private synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		try {
			renderThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// init
		init();
		render();
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 16666666.666666666;
		double delta = 0.0D;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0D) {
				render();
				delta -= 1.0D;
				frames += 1;
				if (System.currentTimeMillis() - timer > 1000L) {
					timer += 1000L;
					currentFrames = frames;
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

	private synchronized void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		graphics = bs.getDrawGraphics();

		// ********** RENDER BEGIN ***********

		currentGameState.render(graphics);

		// *********** RENDER END ************

		graphics.dispose();
		bs.show();
		bs.dispose();
	}

	public static void stopMainTickThread() {
		runTickThread = false;
	}

	public static void restartMainTickThread() {
		runTickThread = true;
		tickThread = new Thread(tick);
		tickThread.setPriority(2);
		tickThread.setName("Main-Tick");
		tickThread.start();
	}

	public static short getFPS() {
		return currentFrames;
	}

}