package net.egartley.beyondorigins.engine;

import java.util.concurrent.ThreadLocalRandom;

public class Util {

    public static int randomInt(int min, int max) {
        if (max < min) {
            int actualmin = max;
            max = min;
            min = actualmin;
        }
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static int randomInt(int min, int max, boolean inclusive) {
        if (inclusive) {
            return randomInt(min, max + 1);
        } else {
            return randomInt(min, max);
        }
    }

    public static boolean fiftyFifty() {
        return percentChance(0.5D);
    }

    public static boolean percentChance(double percent) {
        if (percent > 1.0D) {
            percent = 1.0D;
        }
        return randomInt(100, 1, true) <= percent * 100;
    }

}
