package net.egartley.gamelib.threads;

public class DelayedEvent implements Runnable {

    private boolean isRunning;

    /**
     * The amount of time to wait (in seconds)
     */
    public double duration;
    public Thread thread;

    /**
     * Create a new delayed event, which will call {@link #onFinish()} after the specified amount of time
     *
     * @param duration How long to wait, in seconds
     */
    public DelayedEvent(double duration) {
        this.duration = duration;
        thread = new Thread(this, "TimedEvent-" + this.hashCode());
        ThreadBroker.register(thread);
    }

    /**
     * Starts the delayed event
     */
    public void start() {
        isRunning = true;
        thread.start();
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
                e.printStackTrace();
            }
            onFinish();
        }
        isRunning = false;
        ThreadBroker.deregister(thread);
    }

}
