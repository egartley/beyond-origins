package net.egartley.beyondorigins.controllers;

import net.egartley.gamelib.logic.dialogue.CharacterDialogue;
import net.egartley.gamelib.logic.dialogue.DialogueExchange;
import net.egartley.gamelib.logic.events.CharacterDialogueFinishedEvent;
import net.egartley.gamelib.logic.events.DialogueExchangeFinishedEvent;

import java.util.ArrayList;

public class DialogueController {

    private static ArrayList<CharacterDialogueFinishedEvent> characterDialogueFinishedEvents = new ArrayList<>();
    private static ArrayList<DialogueExchangeFinishedEvent> dialogueExchangeFinishedEvents = new ArrayList<>();

    public static void onFinished(CharacterDialogue dialogue) {
        for (CharacterDialogueFinishedEvent event : characterDialogueFinishedEvents) {
            if (event.dialogue.equals(dialogue)) {
                event.onFinish();
                break;
            }
        }
    }

    public static void onFinished(DialogueExchange exchange) {
        for (DialogueExchangeFinishedEvent event : dialogueExchangeFinishedEvents) {
            if (event.exchange.equals(exchange)) {
                event.onFinish();
                break;
            }
        }
    }

    public static void addFinished(CharacterDialogueFinishedEvent event) {
        if (!characterDialogueFinishedEvents.contains(event)) {
            characterDialogueFinishedEvents.add(event);
        }
    }

    public static void addFinished(DialogueExchangeFinishedEvent event) {
        if (!dialogueExchangeFinishedEvents.contains(event)) {
            dialogueExchangeFinishedEvents.add(event);
        }
    }

    public static void removeFinished(CharacterDialogueFinishedEvent event) {
        characterDialogueFinishedEvents.remove(event);
    }

    public static void removeFinished(DialogueExchangeFinishedEvent event) {
        dialogueExchangeFinishedEvents.remove(event);
    }

}
