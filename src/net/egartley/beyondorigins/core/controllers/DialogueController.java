package net.egartley.beyondorigins.core.controllers;

import net.egartley.beyondorigins.core.logic.dialogue.DialogueExchange;
import net.egartley.beyondorigins.core.logic.events.DialogueExchangeFinishedEvent;

import java.util.ArrayList;

public class DialogueController {

    private static final ArrayList<DialogueExchangeFinishedEvent> dialogueExchangeFinishedEvents = new ArrayList<>();

    public static void onFinished(DialogueExchange exchange) {
        for (DialogueExchangeFinishedEvent event : dialogueExchangeFinishedEvents) {
            if (event.exchange.equals(exchange)) {
                event.onFinish();
                break;
            }
        }
    }

    public static void addFinished(DialogueExchangeFinishedEvent event) {
        if (!dialogueExchangeFinishedEvents.contains(event)) {
            dialogueExchangeFinishedEvents.add(event);
        }
    }

    public static void removeFinished(DialogueExchangeFinishedEvent event) {
        dialogueExchangeFinishedEvents.remove(event);
    }

}
