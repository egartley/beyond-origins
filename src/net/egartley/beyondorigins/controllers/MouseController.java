package net.egartley.beyondorigins.controllers;

import net.egartley.gamelib.input.MouseClicked;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MouseController {

    private static ArrayList<MouseClicked> clickeds = new ArrayList<>();

    public static void onMouseClick(MouseEvent e) {
        // TODO: avoid ccm better, works for now
        ((ArrayList<MouseClicked>) clickeds.clone()).forEach(mc -> mc.onClick(e));
    }

    public static void addMouseClicked(MouseClicked mc) {
        if (!clickeds.contains(mc)) {
            clickeds.add(mc);
        }
    }

    public static void removeMouseClicked(MouseClicked mc) {
        clickeds.remove(mc);
    }

}
