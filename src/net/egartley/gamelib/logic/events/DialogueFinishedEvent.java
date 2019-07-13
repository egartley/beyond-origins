package net.egartley.gamelib.logic.events;

import net.egartley.beyondorigins.ingame.CharacterDialogue;

public class DialogueFinishedEvent {

    public CharacterDialogue dialogue;

    public DialogueFinishedEvent(CharacterDialogue dialogue) {
        this.dialogue = dialogue;
    }

    public void onFinish() {

    }

}
