package net.egartley.beyondorigins.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import net.egartley.beyondorigins.Game;

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
		if (pressed.indexOf(e.getKeyCode()) != -1) {
			pressed.remove(pressed.indexOf(e.getKeyCode()));
		}
		if (e.getKeyCode() == KeyEvent.VK_F3) {
			Game.drawBoundaries = !Game.drawBoundaries;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
