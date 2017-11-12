package net.egartley.beyondorigins;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.objects.GameState;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 8213282993283826186L;
	private static boolean running = false;
	
	public static short frames, currentFrames;
	public static Graphics graphics;
	public static JFrame frame;
	public static Dimension d = new Dimension(65 * 16, 65 * 9);
	
	public Thread mainThread;
	
	public static GameState currentGameState;
	
	private void init() {
		currentGameState = new InGameState();
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.setPreferredSize(d);
		game.setMaximumSize(d);
		game.setMinimumSize(d);
		frame = new JFrame("Beyond Origins");
		frame.setSize(d.width, d.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.start();
	}
	
	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		mainThread = new Thread(this);
		mainThread.setPriority(1);
		mainThread.start();
	}
	
	public synchronized void stop() {
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
				tick();
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
		// main render method
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		graphics = bs.getDrawGraphics();

		// ********** RENDER BEGIN ***********
		currentGameState.render(graphics);
		// *********** RENDER END ************
		
		graphics.dispose();
		bs.show();
	}
	
	public synchronized void tick() {
		currentGameState.tick();
	}
	
	public static short getCurrentFramesPerSecond() {
		return currentFrames;
	}

}