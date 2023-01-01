package net.egartley.beyondorigins.engine.controllers;

import net.egartley.beyondorigins.engine.logic.dialogue.DialogueExchange;
import net.egartley.beyondorigins.engine.logic.events.DialogueFinishedEvent;

import java.util.ArrayList;

public class DialogueController {

    private static final ArrayList<DialogueFinishedEvent> dialogueFinishedEvents = new ArrayList<>();

    public static void onFinished(DialogueExchange exchange) {
        for (DialogueFinishedEvent event : dialogueFinishedEvents) {
            if (event.exchange.equals(exchange)) {
                event.onFinish();
                break;
            }
        }
    }

    public static void addFinished(DialogueFinishedEvent event) {
        if (!dialogueFinishedEvents.contains(event)) {
            dialogueFinishedEvents.add(event);
        }
    }

    public static void removeFinished(DialogueFinishedEvent event) {
        dialogueFinishedEvents.remove(event);
    }

}
