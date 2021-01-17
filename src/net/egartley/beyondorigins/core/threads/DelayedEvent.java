package net.egartley.beyondorigins.core.threads;

import net.egartley.beyondorigins.Debug;

public class DelayedEvent implements Runnable {

    private boolean naturalStop = true;

    public double duration;
    public boolean isRunning;
    public Thread thread;

    /**
     * Create a new delayed event, which will call {@link #onFinish()} after the specified amount of time
     *
     * @param duration How long to wait, in seconds
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
     * Call {@link #onFinish()} now ("fast forward" through the delay)
     */
    public void fastForward() {
        naturalStop = true;
        thread.interrupt();
    }

    /**
     * Called after {@link #duration} has passed (after calling {@link #start()})
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
