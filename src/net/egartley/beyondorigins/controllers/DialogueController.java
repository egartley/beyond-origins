package net.egartley.beyondorigins.controllers;

import net.egartley.gamelib.logic.dialogue.DialogueExchange;
import net.egartley.gamelib.logic.events.DialogueExchangeFinishedEvent;

import java.util.ArrayList;

public class DialogueController {

    private static ArrayList<DialogueExchangeFinishedEvent> dialogueExchangeFinishedEvents = new ArrayList<>();

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
