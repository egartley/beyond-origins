package net.egartley.gamelib.threads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadBroker {

    public static ArrayList<Thread> threads = new ArrayList<>();

    public static void register(Thread thread) {
        threads.add(thread);
    }

    public static void deregister(Thread thread) {
        threads.remove(thread);
    }

    public static void killAll() {
        loopKill(threads);
    }

    public static void kill(Thread... toKill) {
        loopKill(Arrays.asList(toKill));
    }

    private static void loopKill(List<Thread> threads) {
        for (Thread t : threads) {
            deregister(t);
        }
        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
