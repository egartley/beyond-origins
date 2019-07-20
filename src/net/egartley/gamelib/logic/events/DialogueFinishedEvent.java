package net.egartley.gamelib.logic.events;

import net.egartley.gamelib.objects.CharacterDialogue;

public class DialogueFinishedEvent {

    public CharacterDialogue dialogue;

    public DialogueFinishedEvent(CharacterDialogue dialogue) {
        this.dialogue = dialogue;
    }

    public void onFinish() {

    }

}
