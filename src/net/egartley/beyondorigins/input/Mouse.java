package net.egartley.beyondorigins.input;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.ui.MenuButton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

    // private static boolean isPressed = false;

    public static int x, y;

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO: some kind of global "on click" thing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // isPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // isPressed = false;
        if (Game.currentGameState.equals(Game.mainMenuState)) {
            for (MenuButton b : Game.mainMenuState.buttons) {
                b.checkClick(e);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
}
