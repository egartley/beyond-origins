package net.egartley.beyondorigins.controllers;

import net.egartley.gamelib.logic.events.DialogueFinishedEvent;
import net.egartley.gamelib.objects.CharacterDialogue;

import java.util.ArrayList;

public class DialogueController {

    private static ArrayList<DialogueFinishedEvent> events = new ArrayList<>();

    public static void onFinished(CharacterDialogue dialogue) {
        for (DialogueFinishedEvent event : events) {
            if (event.dialogue.equals(dialogue)) {
                event.onFinish();
            }
        }
    }

    public static void addFinished(DialogueFinishedEvent event) {
        if (!events.contains(event)) {
            events.add(event);
        }
    }

    public static void removeFinished(DialogueFinishedEvent event) {
        events.remove(event);
    }

}
