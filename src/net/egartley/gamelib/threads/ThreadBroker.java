package net.egartley.gamelib.threads;

import net.egartley.beyondorigins.Debug;

import java.util.ArrayList;
import java.util.Arrays;

public class ThreadBroker {

    public static ArrayList<Thread> threads = new ArrayList<>();

    public static void register(Thread thread) {
        // Debug.out("Registering \"" + thread.getName() + "\"");
        threads.add(thread);
    }

    public static void deregister(Thread thread) {
        // Debug.out("De-registering \"" + thread.getName() + "\"");
        threads.remove(thread);
    }

    public static void kill(Thread toKill) {
        deregister(toKill);
        try {
            toKill.join();
        } catch (InterruptedException e) {
            Debug.error(e);
        }
    }

    public static void killAll() {
        for (Object t : Arrays.copyOf(threads.toArray(), threads.size())) {
            kill((Thread) t);
        }
    }

}
