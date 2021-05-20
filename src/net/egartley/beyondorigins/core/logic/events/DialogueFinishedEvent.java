package net.egartley.beyondorigins.core.logic.events;

import net.egartley.beyondorigins.core.logic.dialogue.DialogueExchange;

/**
 * An event that's called when a dialogue exchange has finished
 */
public class DialogueFinishedEvent {

    public DialogueExchange exchange;

    public DialogueFinishedEvent(DialogueExchange exchange) {
        this.exchange = exchange;
    }

    public void onFinish() {

    }

}
