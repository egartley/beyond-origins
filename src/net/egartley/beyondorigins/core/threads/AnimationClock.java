package net.egartley.beyondorigins.core.threads;

import net.egartley.beyondorigins.core.graphics.Animation;

public class AnimationClock implements Runnable {

    public boolean isRunning;
    public Thread thread;

    int frameDelay;
    Animation animation;

    public AnimationClock(Animation animation) {
        this.animation = animation;
        this.frameDelay = animation.frameDelay;
        thread = new Thread(this, "AnimationClock-" + animation.hashCode());
    }

    public void start() {
        isRunning = true;
        thread.start();
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(frameDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            animation.increment();
        }
    }

}
