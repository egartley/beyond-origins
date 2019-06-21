package net.egartley.gamelib.input;

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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F3:
                Game.debug = !Game.debug;
                break;
            case KeyEvent.VK_SPACE:
                Entities.DIALOGUE_PANEL.advance();
                break;
            case KeyEvent.VK_E:
                Game.inGameState.isInventoryVisible = !Game.inGameState.isInventoryVisible;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
