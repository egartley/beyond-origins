package net.egartley.beyondorigins.threads;

import net.egartley.beyondorigins.Game;

public class MasterTick implements Runnable {

    @Override
    public void run() {
        // system to run 60 times per second (roughly, could be slightly slower on
        // older/cheaper machines)
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double ns = 16666666.666666666;
        double delta = 0.0D;
        while (Game.running && Game.runTickThread) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0D) {
                tick();
                delta -= 1.0D;
                if (System.currentTimeMillis() - timer > 1000L) {
                    timer += 1000L;
                }
            }
            // this helps to stabilize the tick system
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

    private synchronized void tick() {
        if (Game.currentGameState != null) {
            // null check because the first call would have Game.currentGameState as null
            // TODO: Fix?
            Game.currentGameState.tick();
        }
    }

}
