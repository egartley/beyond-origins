package net.egartley.beyondorigins.threads;

import net.egartley.beyondorigins.Game;

public class Tick implements Runnable {

	public static short frames, currentFrames;

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 16666666.666666666;
		double delta = 0.0D;
		while (Game.running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0D) {
				tick();
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
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		if (Game.currentGameState != null) {
			Game.currentGameState.tick();
		}
	}

}