package net.egartley.gamelib.logic.events;

import net.egartley.gamelib.logic.dialogue.DialogueExchange;

public class DialogueExchangeFinishedEvent {

    public DialogueExchange exchange;

    public DialogueExchangeFinishedEvent(DialogueExchange exchange) {
        this.exchange = exchange;
    }

    public void onFinish() {

    }

}
