package net.egartley.beyondorigins.core.threads;

import net.egartley.beyondorigins.Debug;

/**
 * An event that is executed after a delay
 */
public class DelayedEvent implements Runnable {

    private boolean naturalStop = true;

    public double duration;
    public boolean isRunning;
    public Thread thread;

    /**
     * Create a new delayed event, which will execute after the amount of time (seconds)
     */
    public DelayedEvent(double duration) {
        this.duration = duration;
        thread = new Thread(this, "DelayedEvent-" + this.hashCode());
    }

    /**
     * Starts the delayed event
     */
    public void start() {
        isRunning = true;
        thread.start();
    }

    /**
     * Cancel the delayed event, and kill its thread. {@link #onFinish()} is not called
     */
    public void cancel() {
        naturalStop = false;
        thread.interrupt();
    }

    /**
     * Executes the event now, regardless of delay
     */
    public void fastForward() {
        naturalStop = true;
        thread.interrupt();
    }

    /**
     * Actually execute the event (either by fast-forward or delay)
     */
    public void onFinish() {

    }

    @Override
    public void run() {
        if (isRunning) {
            try {
                Thread.sleep((long) (duration * 1000.0D));
            } catch (InterruptedException e) {
                Debug.warning("Delayed event was killed");
            }
            if (naturalStop) {
                onFinish();
            }
        }
        isRunning = false;
    }

}
