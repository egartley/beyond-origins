package net.egartley.gamelib.input;

import net.egartley.beyondorigins.controllers.KeyboardController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Keyboard implements KeyListener {

    private static ArrayList<Integer> pressedKeyCodes = new ArrayList<>();
    private static ArrayList<Integer> invalidatedKeys = new ArrayList<>();

    /**
     * "Invalidate" the given key, which means that it will need to be "re-pressed" in order to appear in {@link #pressedKeyCodes} again
     *
     * @param keyCode The key code of the key to invalidate
     */
    public static void invalidateKey(int keyCode) {
        invalidatedKeys.add(keyCode);
        pressedKeyCodes.remove((Integer) keyCode);
    }

    /**
     * Returns whether or not the specified key is currently being pressed down
     *
     * @param keyCode The key code from {@link java.awt.event.KeyEvent KeyEvent}
     * @return Whether or not the specified key is currently being pressed down
     */
    public static boolean isKeyPressed(int keyCode) {
        return pressedKeyCodes.contains(keyCode);
    }

    public static ArrayList<Integer> pressed() {
        return pressedKeyCodes;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (!pressedKeyCodes.contains(keycode) && !invalidatedKeys.contains(keycode)) {
            pressedKeyCodes.add(keycode);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeyCodes.remove((Integer) keyCode);
        invalidatedKeys.remove((Integer) keyCode);
        KeyboardController.onKeyTyped(keyCode);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
