package net.egartley.beyondorigins;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.Player;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.objects.GameState;
import net.egartley.beyondorigins.objects.SpriteSheet;
import net.egartley.beyondorigins.threads.Tick;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 8213282993283826186L;
	public static boolean running = false;

	public static short frames, currentFrames;
	public static Graphics graphics;
	public static JFrame frame;
	public static Dimension windowDimension = new Dimension(1040, 585);

	public Thread renderThread;
	public Thread tickThread;

	private Tick tick = new Tick();

	public static GameState currentGameState;

	private void init() {
		load();
		currentGameState = new InGameState();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.setPreferredSize(windowDimension);
		game.setMaximumSize(windowDimension);
		game.setMinimumSize(windowDimension);
		frame = new JFrame("Beyond Origins INDEV");
		frame.setSize(windowDimension.width, windowDimension.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.start();
	}

	public static void load() {
		// *********** PLAYER BEGIN ***********
		BufferedImage playerImage = null;
		try {
			playerImage = ImageIO.read(new File("resources/images/player-default.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte scale = 2;
		if (playerImage != null) {
			playerImage = Util.resized(playerImage, playerImage.getWidth() * scale, playerImage.getHeight() * scale);
		}
		Entities.PLAYER = new Player(new SpriteSheet(playerImage, 15 * scale, 23 * scale, 2, 4).getSpriteCollection());
		// ************ PLAYER END ************
	}

	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;

		renderThread = new Thread(this);
		tickThread = new Thread(tick);

		renderThread.setPriority(1);
		tickThread.setPriority(2);

		renderThread.setName("Render");
		tickThread.setName("Tick");

		tickThread.start();
		renderThread.start();
	}

	public synchronized void stop() {
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

	public synchronized void render() {
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

	public static short getCurrentFramesPerSecond() {
		return currentFrames;
	}

}