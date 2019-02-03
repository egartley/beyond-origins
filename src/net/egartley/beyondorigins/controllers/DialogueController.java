package net.egartley.beyondorigins.controllers;

import net.egartley.beyondorigins.entities.Entities;

import java.awt.*;

public class DialogueController {

    public static void render(Graphics graphics) {
        Entities.DIALOGUE_PANEL.render(graphics);
    }

    public static void tick() {
        Entities.DIALOGUE_PANEL.tick();
    }

}
