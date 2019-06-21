package net.egartley.gamelib.input;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.ui.MenuButton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

    public static int x, y;
    public static boolean isDragging;

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO: some kind of global "on click" thing
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (Game.currentGameState.equals(Game.mainMenuState)) {
            for (MenuButton b : Game.mainMenuState.buttons) {
                b.checkClick(e);
            }
        }
        isDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        isDragging = true;
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
}
