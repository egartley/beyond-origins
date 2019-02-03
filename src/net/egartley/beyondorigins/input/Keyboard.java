package net.egartley.beyondorigins.input;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.Entities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Keyboard implements KeyListener {

    private static ArrayList<Integer> pressedKeyCodes = new ArrayList<Integer>();

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
        int i = pressedKeyCodes.indexOf(e.getKeyCode());
        if (i != -1) {
            pressedKeyCodes.remove(i);
        }
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            Game.debug = !Game.debug;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Entities.DIALOGUE_PANEL.nextLine();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
