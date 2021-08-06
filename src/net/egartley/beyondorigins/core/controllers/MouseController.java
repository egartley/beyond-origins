package net.egartley.beyondorigins.core.controllers;

import net.egartley.beyondorigins.core.input.MouseClickedEvent;

import java.util.ArrayList;

public class MouseController {

    private static final ArrayList<MouseClickedEvent> clickeds = new ArrayList<>();

    public static void onMouseClick(int x, int y) {
        ((ArrayList<MouseClickedEvent>) clickeds.clone()).forEach(mc -> mc.onClick(x, y));
    }

    public static void addMouseClicked(MouseClickedEvent mc) {
        if (!clickeds.contains(mc)) {
            clickeds.add(mc);
        }
    }

    public static void removeMouseClicked(MouseClickedEvent mc) {
        clickeds.remove(mc);
    }

}
