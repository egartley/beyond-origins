package net.egartley.beyondorigins.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import net.egartley.beyondorigins.entities.Entities;

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
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_EQUALS) {
			Entities.PLAYER.speed += 0.1;
		} else if (code == KeyEvent.VK_MINUS) {
			Entities.PLAYER.speed -= 0.1;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
