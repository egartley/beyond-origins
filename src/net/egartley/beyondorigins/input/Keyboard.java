package net.egartley.beyondorigins.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import net.egartley.beyondorigins.Game;

public class Keyboard implements KeyListener {

	private static ArrayList<Integer> pressedKeyCodes = new ArrayList<Integer>();

	public static boolean isPressed(int keycode) {
		return pressedKeyCodes.contains(keycode);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		if (!isPressed(keycode)) {
			pressedKeyCodes.add(keycode);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (pressedKeyCodes.indexOf(e.getKeyCode()) != -1) {
			pressedKeyCodes.remove(pressedKeyCodes.indexOf(e.getKeyCode()));
		}
		if (e.getKeyCode() == KeyEvent.VK_F3) {
			Game.debug = !Game.debug;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
