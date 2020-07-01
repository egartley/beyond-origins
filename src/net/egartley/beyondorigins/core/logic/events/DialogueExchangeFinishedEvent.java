package net.egartley.beyondorigins.core.logic.events;

import net.egartley.beyondorigins.core.logic.dialogue.DialogueExchange;

public class DialogueExchangeFinishedEvent {

    public DialogueExchange exchange;

    public DialogueExchangeFinishedEvent(DialogueExchange exchange) {
        this.exchange = exchange;
    }

    public void onFinish() {

    }

}
