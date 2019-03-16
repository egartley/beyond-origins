package net.egartley.beyondorigins.threads;

import net.egartley.beyondorigins.graphics.Animation;

public class AnimationClock implements Runnable {

    public boolean isRunning;

    int frameDelay;
    Animation animation;
    Thread thread;

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
            // System.out.println(thread.getName() + " switching frame");
            animation.increment();
        }
    }

}
