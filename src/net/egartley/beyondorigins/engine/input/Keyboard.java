package net.egartley.beyondorigins.engine.input;

import java.util.ArrayList;

public class Keyboard {

    private static final ArrayList<Integer> pressedKeyCodes = new ArrayList<>();
    private static final ArrayList<Integer> invalidatedKeys = new ArrayList<>();

    /**
     * "Invalidate" the given key, which means that it will need to be "re-pressed" in order to appear in {@link #pressedKeyCodes} again
     */
    public static void invalidateKey(int keyCode) {
        invalidatedKeys.add(keyCode);
        pressedKeyCodes.remove((Integer) keyCode);
    }

    public static boolean isPressed(int keyCode) {
        return pressedKeyCodes.contains(keyCode);
    }

    public static ArrayList<Integer> pressed() {
        return pressedKeyCodes;
    }

    public static void keyPressed(int keyCode) {
        if (!pressedKeyCodes.contains(keyCode) && !invalidatedKeys.contains(keyCode)) {
            pressedKeyCodes.add(keyCode);
        }
    }

    public static void keyReleased(int keyCode) {
        pressedKeyCodes.remove((Integer) keyCode);
        invalidatedKeys.remove((Integer) keyCode);
    }

}
