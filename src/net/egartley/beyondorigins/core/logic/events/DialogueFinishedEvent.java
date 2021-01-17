package net.egartley.beyondorigins.core.logic.events;

import net.egartley.beyondorigins.core.logic.dialogue.DialogueExchange;

public class DialogueFinishedEvent {

    public DialogueExchange exchange;

    public DialogueFinishedEvent(DialogueExchange exchange) {
        this.exchange = exchange;
    }

    public void onFinish() {

    }

}
