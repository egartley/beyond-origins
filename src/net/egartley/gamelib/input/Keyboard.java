package net.egartley.gamelib.input;

import net.egartley.beyondorigins.controllers.KeyboardController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Keyboard implements KeyListener {

    private static ArrayList<Integer> pressedKeyCodes = new ArrayList<>();

    /**
     * @param keycode
     *         The key code from {@link java.awt.event.KeyEvent KeyEvent }
     *
     * @return Whether or not the provided key is currently being pressed down
     */
    public static boolean isKeyPressed(int keycode) {
        return pressedKeyCodes.contains(keycode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (!isKeyPressed(keycode)) {
            pressedKeyCodes.add(keycode);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int kc = e.getKeyCode();
        int i = pressedKeyCodes.indexOf(kc);
        if (i != -1) {
            pressedKeyCodes.remove(i);
        }
        KeyboardController.onKeyTyped(kc);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
