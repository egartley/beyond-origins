package net.egartley.beyondorigins.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Keyboard implements KeyListener {

	public static ArrayList<Integer> pressed = new ArrayList<Integer>();

	public static boolean isPressed(int keycode) {
		return pressed.contains(keycode);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		if (!isPressed(keycode)) {
			pressed.add(keycode);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressed.remove(pressed.indexOf(e.getKeyCode()));
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
